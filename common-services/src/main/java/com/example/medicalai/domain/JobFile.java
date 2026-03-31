package com.example.medicalai.domain;

import jakarta.persistence.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "job_file")
public class JobFile {
    @Id
    @Column(columnDefinition = "uuid")
    private UUID id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_id")
    private Job job;
    private String s3Key;
    private String contentType;
    private Instant createdAt;

    public JobFile() {
    }

    public JobFile(UUID id, Job job, String s3Key, String contentType, Instant createdAt) {
        this.id = id;
        this.job = job;
        this.s3Key = s3Key;
        this.contentType = contentType;
        this.createdAt = createdAt;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID v) {
        this.id = v;
    }

    public Job getJob() {
        return job;
    }

    public void setJob(Job v) {
        this.job = v;
    }

    public String getS3Key() {
        return s3Key;
    }

    public void setS3Key(String v) {
        this.s3Key = v;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String v) {
        this.contentType = v;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant v) {
        this.createdAt = v;
    }
}
