package com.example.medicalai.api.controller;

import com.example.medicalai.ai.DocumentProcessingService;
import com.example.medicalai.ai.MedicalSummaryService;
import com.example.medicalai.contracts.FileRef;
import com.example.medicalai.contracts.JobStatusResponse;
import com.example.medicalai.contracts.MedicalSummaryResponse;
import com.example.medicalai.service.JobService;
import com.example.medicalai.service.StorageService;
import com.example.medicalai.service.SummarizeProducer;
import jakarta.validation.constraints.NotEmpty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MimeTypeUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.model.S3Exception;

import java.io.IOException;
import java.util.*;

@RestController
@RequestMapping("/api/reports")
@Validated
public class ReportController {

    private static final Logger log = LoggerFactory.getLogger(ReportController.class);

    private final DocumentProcessingService docService;
    private final MedicalSummaryService summaryService;
    private final StorageService storage;
    private final JobService jobService;
    private final SummarizeProducer producer;

    public ReportController(DocumentProcessingService docService,
                            MedicalSummaryService summaryService,
                            StorageService storage,
                            JobService jobService,
                            SummarizeProducer producer) {
        this.docService = docService;
        this.summaryService = summaryService;
        this.storage = storage;
        this.jobService = jobService;
        this.producer = producer;
    }

    @PostMapping(
            path = "/upload",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MimeTypeUtils.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<MedicalSummaryResponse> upload(
            @NotEmpty @RequestParam("files") MultipartFile[] files) throws IOException {

        validateFiles(files);

        List<MultipartFile> validFiles = Arrays.stream(files)
                .filter(Objects::nonNull)
                .filter(file -> !file.isEmpty())
                .toList();

        if (validFiles.isEmpty()) {
            throw new IllegalArgumentException("No valid files provided");
        }

        String combined = docService.extractCombinedText(validFiles);
        MedicalSummaryResponse response = summaryService.generateSummary(combined);
    System.out.println(response.toString());

        return ResponseEntity.ok(response);
    }

    @PostMapping(
            path = "/submit",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MimeTypeUtils.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Map<String, Object>> submit(
            @RequestParam(value = "patientId", required = false) String patientId,
            @NotEmpty @RequestParam("files") MultipartFile[] files) throws IOException {

        validateFiles(files);

        List<FileRef> refs = new ArrayList<>();
        String jobFolder = UUID.randomUUID().toString();
        String safePatientId = normalizePatientId(patientId);

        for (MultipartFile file : files) {
            if (file == null || file.isEmpty()) {
                continue;
            }
            String originalName = file.getOriginalFilename();
            String safeFileName = sanitizeFileName(originalName);
            String contentType = file.getContentType() == null
                    ? MediaType.APPLICATION_OCTET_STREAM_VALUE
                    : file.getContentType();
            String key = "reports/" + safePatientId + "/" + jobFolder + "/" + safeFileName;
            try {
                log.info("Uploading file to S3. key={}, size={}, contentType={}", key, file.getSize(), contentType);
                storage.put(key, file.getBytes(), contentType);
                refs.add(new FileRef(key, contentType));
                log.info("Successfully uploaded file to S3. key={}", key);
            } catch (S3Exception e) {
                log.error("S3 upload failed. key={}, statusCode={}, awsMessage={}",
                        key,
                        e.statusCode(),
                        e.awsErrorDetails() != null ? e.awsErrorDetails().errorMessage() : e.getMessage(),
                        e);
                throw new RuntimeException("Failed to upload file to S3: " + safeFileName, e);
            } catch (Exception e) {
                log.error("Unexpected upload error for key={}", key, e);
                throw new RuntimeException("Failed to upload file: " + safeFileName, e);
            }
        }

        if (refs.isEmpty()) {
            throw new IllegalArgumentException("No valid files provided for upload");
        }

        UUID jobId = jobService.createJob(patientId, refs);

        producer.publish(jobId.toString(), patientId, refs);

        Map<String, Object> response = new HashMap<>();
        response.put("jobId", jobId.toString());
        response.put("status", "PENDING");
        response.put("fileCount", refs.size());

        return ResponseEntity.accepted().body(response);
    }

    @GetMapping(path = "/status/{jobId}", produces = MimeTypeUtils.APPLICATION_JSON_VALUE)
    public ResponseEntity<JobStatusResponse> status(@PathVariable("jobId") String jobId) {
        return jobService.getStatus(UUID.fromString(jobId))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    private void validateFiles(MultipartFile[] files) {
        if (files == null || files.length == 0) {
            throw new IllegalArgumentException("At least one file is required");
        }

        for (MultipartFile file : files) {
            if (file == null || file.isEmpty()) {
                continue;
            }

            String fileName = file.getOriginalFilename() == null ? "" : file.getOriginalFilename().toLowerCase();
            String contentType = file.getContentType() == null ? "" : file.getContentType().toLowerCase();

            boolean supported =
                    MediaType.APPLICATION_PDF_VALUE.equals(contentType)
                            || contentType.startsWith("image/")
                            || fileName.endsWith(".pdf")
                            || fileName.endsWith(".jpg")
                            || fileName.endsWith(".jpeg")
                            || fileName.endsWith(".png");

            if (!supported) {
                throw new IllegalArgumentException("Unsupported file type: " + file.getOriginalFilename());
            }
        }
    }

    private String sanitizeFileName(String fileName) {
        if (fileName == null || fileName.isBlank()) {
            return "file";
        }

        return fileName
                .replace("\\", "_")
                .replace("/", "_")
                .replace("..", "_")
                .replaceAll("[^a-zA-Z0-9._-]", "_");
    }

    private String normalizePatientId(String patientId) {
        if (patientId == null || patientId.isBlank()) {
            return "UNKNOWN";
        }

        return patientId.replaceAll("[^a-zA-Z0-9_-]", "_");
    }
}