package com.example.medicalai.worker.kafka;

import com.example.medicalai.contracts.FileRef;
import com.example.medicalai.contracts.MedicalSummaryResponse;
import com.example.medicalai.contracts.SummarizeRequestedEvent;
import com.example.medicalai.contracts.SummarizeProcessingEvent;
import com.example.medicalai.contracts.SummarizeCompletedEvent;
import com.example.medicalai.contracts.SummarizeFailedEvent;
import com.example.medicalai.service.StorageService;
import com.example.medicalai.ai.DocumentProcessingService;
import com.example.medicalai.ai.MedicalSummaryService;
import com.example.worker.util.ByteArrayMultipartFile;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class SummarizeConsumer {

    private final StorageService storage;
    private final DocumentProcessingService docService;
    private final MedicalSummaryService summaryService;
    private final KafkaTemplate<String, Object> kafka; // to publish status

    public SummarizeConsumer(StorageService st,
                             DocumentProcessingService ds,
                             MedicalSummaryService ms,
                             KafkaTemplate<String, Object> kafka) {
        this.storage = st;
        this.docService = ds;
        this.summaryService = ms;
        this.kafka = kafka;
    }

    @KafkaListener(topics = "medical.summarize.requests", groupId = "summarizer")
    public void onMessage(ConsumerRecord<String, SummarizeRequestedEvent> rec) {
        SummarizeRequestedEvent evt = rec.value();
        UUID jobId = UUID.fromString(evt.getJobId());

        try {
            // 1) Publish "processing" event (optional, but nice for UI)
            kafka.send("medical.summarize.status",
                    jobId.toString(),
                    new SummarizeProcessingEvent(jobId.toString(), Instant.now()));

            // 2) Download files and build a List<MultipartFile>
            List<MultipartFile> files = new ArrayList<>();
            for (FileRef fr : evt.getFiles()) {
                try (InputStream in = storage.get(fr.getS3Key())) {
                    byte[] bytes = in.readAllBytes();
                    files.add(new ByteArrayMultipartFile(
                            "files",
                            fr.getS3Key(),       // or Paths.get(fr.getS3Key()).getFileName().toString()
                            fr.getContentType(),
                            bytes
                    ));
                }
            }

            String text = docService.extractCombinedText(files);
            long t0 = System.currentTimeMillis();
            MedicalSummaryResponse resp = summaryService.generateSummary(text);
      System.out.println(resp.toString());
            long dur = System.currentTimeMillis() - t0;

            // 4) Publish "completed" event (API will update DB)
            kafka.send("medical.summarize.completed",
                    jobId.toString(),
                    new SummarizeCompletedEvent(jobId.toString(), resp, dur, Instant.now()));

        } catch (Exception ex) {
            // 5) Publish "failed" event (API will mark job failed)
            kafka.send("medical.summarize.failed",
                    jobId.toString(),
                    new SummarizeFailedEvent(jobId.toString(), "PROCESSING_ERROR", ex.getMessage(), Instant.now()));
        }
    }
}