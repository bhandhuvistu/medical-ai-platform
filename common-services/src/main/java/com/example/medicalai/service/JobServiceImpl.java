package com.example.medicalai.service;

import com.example.medicalai.contracts.FileRef;
import com.example.medicalai.contracts.JobStatusResponse;
import com.example.medicalai.contracts.MedicalSummaryResponse;
import com.example.medicalai.domain.Job;
import com.example.medicalai.domain.JobFile;
import com.example.medicalai.domain.JobResult;
import com.example.medicalai.domain.JobStatus;
import com.example.medicalai.repo.JobFileRepository;
import com.example.medicalai.repo.JobRepository;
import com.example.medicalai.repo.JobResultRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Profile("api")
@Service
public class JobServiceImpl implements JobService {
    private final JobRepository jobRepo;
    private final JobFileRepository jobFileRepo;
    private final JobResultRepository jobResultRepo;

    public JobServiceImpl(JobRepository j, JobFileRepository jf, JobResultRepository jr) {
        this.jobRepo = j;
        this.jobFileRepo = jf;
        this.jobResultRepo = jr;
    }

    @Override
    @Transactional
    public UUID createJob(String patientId, List<FileRef> files) {
        UUID id = UUID.randomUUID();
        Job job = new Job(id, patientId, JobStatus.PENDING, Instant.now(), Instant.now());
        jobRepo.save(job);

        for (FileRef fr : files) {
            JobFile jf = new JobFile(UUID.randomUUID(), job, fr.getS3Key(), fr.getContentType(), Instant.now());
            jobFileRepo.save(jf);
        }
        return id;
    }

    @Override
    @Transactional
    public void markProcessing(UUID jobId) {
        jobRepo.findById(jobId).ifPresent(j -> {
            j.setStatus(JobStatus.PROCESSING);
            j.setUpdatedAt(Instant.now());
            jobRepo.save(j);
        });
    }

    @Override
    @Transactional
    public void markCompleted(UUID jobId, MedicalSummaryResponse summary) {
        jobRepo.findById(jobId).ifPresent(j -> {
            j.setStatus(JobStatus.COMPLETED);
            j.setUpdatedAt(Instant.now());
            jobRepo.save(j);
            JobResult jr = new JobResult(jobId, JsonUtil.toJson(summary), Instant.now());
            jobResultRepo.save(jr);
        });
    }

    @Override
    @Transactional
    public void markFailed(UUID jobId, String code, String message) {
        jobRepo.findById(jobId).ifPresent(j -> {
            j.setStatus(JobStatus.FAILED);
            j.setUpdatedAt(Instant.now());
            jobRepo.save(j);
        });
    }

    @Override
    public Optional<JobStatusResponse> getStatus(UUID jobId) {
        return jobRepo.findById(jobId).map(j -> {
            JobStatusResponse resp = new JobStatusResponse();
            resp.setJobId(j.getId().toString());
            resp.setStatus(j.getStatus().name());
            if (j.getStatus() == JobStatus.COMPLETED) {
                jobResultRepo.findById(j.getId()).ifPresent(r -> {
                    resp.setSummary(JsonUtil.fromJson(r.getJson(), MedicalSummaryResponse.class));
                });
            }
            return resp;
        });
    }
}