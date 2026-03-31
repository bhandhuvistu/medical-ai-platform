package com.example.medicalai.contracts;

public class FileRef {
    private String s3Key;
    private String contentType;

    public FileRef() {
    }

    public FileRef(String s3Key, String contentType) {
        this.s3Key = s3Key;
        this.contentType = contentType;
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
}
