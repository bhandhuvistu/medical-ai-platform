package com.example.medicalai.service;

import com.example.medicalai.contracts.FileRef;

import java.util.List;

public interface SummarizeProducer {
    void publish(String jobId, String patientId, List<FileRef> files);
}
