package com.example.medicalai.service;

import java.io.InputStream;
import java.net.URL;
import java.time.Duration;

public interface StorageService {
//  String put(String key, InputStream in, long size, String contentType);

  InputStream get(String key);

  default URL presignPut(String key, Duration ttl) {
    return null;
  }

  default URL presignGet(String key, Duration ttl) {
    return null;
  }

  String put(String key, byte[] content, String contentType);
}
