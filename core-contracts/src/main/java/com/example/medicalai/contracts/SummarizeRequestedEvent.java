package com.example.medicalai.contracts;

import java.time.Instant;
import java.util.List;

public class SummarizeRequestedEvent {
    private int schemaVersion = 1;
    private String jobId;
    private String patientId;
    private List<FileRef> files;
    private String requestedBy;
    private Instant requestedAt;

    public int getSchemaVersion() {
        return schemaVersion;
    }

    public void setSchemaVersion(int v) {
        this.schemaVersion = v;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String v) {
        this.jobId = v;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String v) {
        this.patientId = v;
    }

    public List<FileRef> getFiles() {
        return files;
    }

    public void setFiles(List<FileRef> v) {
        this.files = v;
    }

    public String getRequestedBy() {
        return requestedBy;
    }

    public void setRequestedBy(String v) {
        this.requestedBy = v;
    }

    public Instant getRequestedAt() {
        return requestedAt;
    }

    public void setRequestedAt(Instant v) {
        this.requestedAt = v;
    }
}
