package com.example.medicalai.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.http.apache.ApacheHttpClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3ClientBuilder;

import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.cert.X509Certificate;

@Configuration
public class S3Config {

  @Bean
  public S3Client s3Client(
          @Value("${aws.region}") String region,
          @Value("${aws.s3.insecure-skip-ssl:false}") boolean insecureSkipSsl
  ) {
    S3ClientBuilder builder = S3Client.builder()
            .region(Region.of(region))
            .credentialsProvider(DefaultCredentialsProvider.create());

    if (insecureSkipSsl) {
      TrustManager[] trustAll = new TrustManager[] {
              new X509TrustManager() {
                @Override
                public void checkClientTrusted(X509Certificate[] chain, String authType) {
                }

                @Override
                public void checkServerTrusted(X509Certificate[] chain, String authType) {
                }

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                  return new X509Certificate[0];
                }
              }
      };

      builder.httpClientBuilder(
              ApacheHttpClient.builder()
                      .tlsTrustManagersProvider(() -> trustAll)
      );
    } else {
      builder.httpClientBuilder(ApacheHttpClient.builder());
    }

    return builder.build();
  }
}