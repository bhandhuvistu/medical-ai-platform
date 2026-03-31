//package com.example.medicalai.config;
//
//import jakarta.annotation.PostConstruct;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Profile;
//import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
//import software.amazon.awssdk.http.urlconnection.UrlConnectionHttpClient;
//import software.amazon.awssdk.regions.Region;
//import software.amazon.awssdk.services.s3.S3Client;
//
//import javax.net.ssl.HostnameVerifier;
//import javax.net.ssl.HttpsURLConnection;
//import javax.net.ssl.SSLContext;
//import javax.net.ssl.SSLSession;
//import javax.net.ssl.TrustManager;
//import javax.net.ssl.X509TrustManager;
//import java.security.SecureRandom;
//import java.security.cert.X509Certificate;
//
//@Configuration
//public class BypassSSLConfig {
//
//
//  @PostConstruct
//  public void init() {
//    System.out.println(">>> BypassSSLConfig loaded <<<");
//  }
//
//  @PostConstruct
//  public void disableSslVerification() throws Exception {
//    TrustManager[] trustAllCerts =
//        new TrustManager[] {
//          new X509TrustManager() {
//            @Override
//            public void checkClientTrusted(X509Certificate[] chain, String authType) {}
//
//            @Override
//            public void checkServerTrusted(X509Certificate[] chain, String authType) {}
//
//            @Override
//            public X509Certificate[] getAcceptedIssuers() {
//              return new X509Certificate[0];
//            }
//          }
//        };
//    System.out.println("_______________ created _____________");
//
//    SSLContext sslContext = SSLContext.getInstance("TLS");
//    sslContext.init(null, trustAllCerts, new SecureRandom());
//
//    HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
//
//    HostnameVerifier allHostsValid =
//        new HostnameVerifier() {
//          @Override
//          public boolean verify(String hostname, SSLSession session) {
//            return true;
//          }
//        };
//
//    HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
//  }
//
//}
