package com.example.medicalai.api.config;

import com.example.medicalai.contracts.FileRef;
import com.example.medicalai.contracts.SummarizeRequestedEvent;
import com.example.medicalai.service.SummarizeProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;

@Component
public class KafkaSummarizeProducerApi implements SummarizeProducer {
    private final KafkaTemplate<String, Object> template;

    public KafkaSummarizeProducerApi(KafkaTemplate<String, Object> template) {
        this.template = template;
    }

    @Override
    public void publish(String jobId, String patientId, List<FileRef> files) {
        SummarizeRequestedEvent evt = new SummarizeRequestedEvent();
        evt.setJobId(jobId);
        evt.setPatientId(patientId);
        evt.setFiles(files);
        evt.setRequestedBy("api");
        evt.setRequestedAt(Instant.now());
        template.send(new ProducerRecord<>("medical.summarize.requests", jobId, evt));
    }
}
