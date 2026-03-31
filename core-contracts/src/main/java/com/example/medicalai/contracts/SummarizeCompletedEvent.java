package com.example.medicalai.contracts;

import java.time.Instant;

public class SummarizeCompletedEvent {
    private String jobId;
    private MedicalSummaryResponse response;
    private long durationMillis;
    private Instant at;

    public SummarizeCompletedEvent() {} // for Jackson

    public SummarizeCompletedEvent(String jobId,
                                   MedicalSummaryResponse response,
                                   long durationMillis,
                                   Instant at) {
        this.jobId = jobId;
        this.response = response;
        this.durationMillis = durationMillis;
        this.at = at;
    }

    public String getJobId() { return jobId; }
    public void setJobId(String jobId) { this.jobId = jobId; }

    public MedicalSummaryResponse getResponse() { return response; }
    public void setResponse(MedicalSummaryResponse response) { this.response = response; }

    public long getDurationMillis() { return durationMillis; }
    public void setDurationMillis(long durationMillis) { this.durationMillis = durationMillis; }

    public Instant getAt() { return at; }
    public void setAt(Instant at) { this.at = at; }
}