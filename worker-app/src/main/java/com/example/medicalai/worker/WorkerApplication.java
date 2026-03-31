package com.example.medicalai.worker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(

        scanBasePackages = {
                "com.example.medicalai.worker",     // worker components
                "com.example.medicalai.worker.messaging", // logger/consumers you added
                "com.example.medicalai.ai",         // AI/OCR components (no JPA)
                "com.example.medicalai.s3",          // S3 storage service
                "com.example.medicalai.config"

        }
)
public class WorkerApplication {
    public static void main(String[] args) {
       SpringApplication.run(WorkerApplication.class, args);
    }
}
