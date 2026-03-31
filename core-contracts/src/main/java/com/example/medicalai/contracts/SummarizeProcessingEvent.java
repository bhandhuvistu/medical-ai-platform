package com.example.medicalai.contracts;

import java.time.Instant;

public class SummarizeProcessingEvent {
    private String jobId;
    private Instant at;

    public SummarizeProcessingEvent() {} // for Jackson

    public SummarizeProcessingEvent(String jobId, Instant at) {
        this.jobId = jobId;
        this.at = at;
    }

    public String getJobId() { return jobId; }
    public void setJobId(String jobId) { this.jobId = jobId; }

    public Instant getAt() { return at; }
    public void setAt(Instant at) { this.at = at; }
}