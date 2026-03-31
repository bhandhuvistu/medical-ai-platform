package com.example.medicalai.service;

import com.example.medicalai.contracts.*;

import java.util.*;

public interface JobService {
    UUID createJob(String patientId, java.util.List<FileRef> files);

    void markProcessing(UUID jobId);

    void markCompleted(UUID jobId, MedicalSummaryResponse summary);

    void markFailed(UUID jobId, String errorCode, String errorMessage);

    Optional<JobStatusResponse> getStatus(UUID jobId);
}
