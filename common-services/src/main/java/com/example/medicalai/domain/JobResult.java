package com.example.medicalai.domain;

import jakarta.persistence.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "job_result")
public class JobResult {
    @Id
    @Column(name = "job_id", columnDefinition = "uuid")
    private UUID jobId;

    @Lob
    private String json;
    private Instant createdAt;

    public JobResult() {
    }

    public JobResult(UUID jobId, String json, Instant createdAt) {
        this.jobId = jobId;
        this.json = json;
        this.createdAt = createdAt;
    }

    public UUID getJobId() {
        return jobId;
    }

    public void setJobId(UUID v) {
        this.jobId = v;
    }

    public String getJson() {
        return json;
    }

    public void setJson(String v) {
        this.json = v;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant v) {
        this.createdAt = v;
    }
}
