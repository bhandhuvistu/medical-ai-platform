package com.example.worker.util;

import org.springframework.web.multipart.MultipartFile;

import java.io.*;

/**
 * Simple MultipartFile backed by a byte[].
 * Useful in production code when you need a MultipartFile
 * but don't want to depend on spring-test's MockMultipartFile.
 */
public class ByteArrayMultipartFile implements MultipartFile {

    private final String name;
    private final String originalFilename;
    private final String contentType;
    private final byte[] bytes;

    public ByteArrayMultipartFile(String name,
                                  String originalFilename,
                                  String contentType,
                                  byte[] bytes) {
        this.name = name;
        this.originalFilename = originalFilename;
        this.contentType = contentType;
        this.bytes = (bytes != null ? bytes : new byte[0]);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getOriginalFilename() {
        return originalFilename;
    }

    @Override
    public String getContentType() {
        return contentType;
    }

    @Override
    public boolean isEmpty() {
        return bytes.length == 0;
    }

    @Override
    public long getSize() {
        return bytes.length;
    }

    @Override
    public byte[] getBytes() {
        return bytes.clone();
    }

    @Override
    public InputStream getInputStream() {
        return new ByteArrayInputStream(bytes);
    }

    @Override
    public void transferTo(File dest) throws IOException {
        // simple implementation writing the bytes to disk
        try (OutputStream out = new FileOutputStream(dest)) {
            out.write(bytes);
        }
    }
}