package com.example.medicalai.worker.config;

import com.example.medicalai.contracts.FileRef;
import com.example.medicalai.service.SummarizeProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class KafkaSummarizeProducer implements SummarizeProducer {
    private final KafkaTemplate<String, Object> template;

    public KafkaSummarizeProducer(KafkaTemplate<String, Object> template) {
        this.template = template;
    }

    @Override
    public void publish(String jobId, String patientId, List<FileRef> files) {
        com.example.medicalai.contracts.SummarizeRequestedEvent evt = new com.example.medicalai.contracts.SummarizeRequestedEvent();
        evt.setJobId(jobId);
        evt.setPatientId(patientId);
        evt.setFiles(files);
        evt.setRequestedBy("api");
        evt.setRequestedAt(java.time.Instant.now());
        template.send(new ProducerRecord<>("medical.summarize.requests", jobId, evt));
    }
}
