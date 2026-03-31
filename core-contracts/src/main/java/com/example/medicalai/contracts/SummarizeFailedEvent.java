package com.example.medicalai.contracts;

import java.time.Instant;

public class SummarizeFailedEvent {
    private String jobId;
    private String code;
    private String message;
    private Instant at;

    public SummarizeFailedEvent() {} // for Jackson

    public SummarizeFailedEvent(String jobId, String code, String message, Instant at) {
        this.jobId = jobId;
        this.code = code;
        this.message = message;
        this.at = at;
    }

    public String getJobId() { return jobId; }
    public void setJobId(String jobId) { this.jobId = jobId; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public Instant getAt() { return at; }
    public void setAt(Instant at) { this.at = at; }
}