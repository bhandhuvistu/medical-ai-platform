package com.example.medicalai.contracts;

public class JobStatusResponse {
    private String jobId;
    private String status;
    private MedicalSummaryResponse summary;
    private String errorCode;
    private String errorMessage;

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String v) {
        this.jobId = v;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String v) {
        this.status = v;
    }

    public MedicalSummaryResponse getSummary() {
        return summary;
    }

    public void setSummary(MedicalSummaryResponse v) {
        this.summary = v;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String v) {
        this.errorCode = v;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String v) {
        this.errorMessage = v;
    }
}
