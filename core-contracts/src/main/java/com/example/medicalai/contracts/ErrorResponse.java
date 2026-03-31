package com.example.medicalai.contracts;

public class ErrorResponse {
    private String error;
    private String message;
    private String code;
    private String traceId;
    private String timestamp;

    public String getError() {
        return error;
    }

    public void setError(String v) {
        this.error = v;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String v) {
        this.message = v;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String v) {
        this.code = v;
    }

    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String v) {
        this.traceId = v;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String v) {
        this.timestamp = v;
    }
}
