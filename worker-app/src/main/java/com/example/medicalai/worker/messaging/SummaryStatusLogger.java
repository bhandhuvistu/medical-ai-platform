package com.example.medicalai.worker.messaging;

import com.example.medicalai.contracts.SummarizeProcessingEvent;
import com.example.medicalai.contracts.SummarizeCompletedEvent;
import com.example.medicalai.contracts.SummarizeFailedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Profile("worker") // only load when the worker profile is active
@Component
public class SummaryStatusLogger {

    private static final Logger log = LoggerFactory.getLogger(SummaryStatusLogger.class);

    @KafkaListener(topics = "medical.summarize.status", groupId = "worker-observer")
    public void onProcessing(SummarizeProcessingEvent evt) {
        log.info("[worker] STATUS: processing jobId={}", evt.getJobId());
    }

    @KafkaListener(topics = "medical.summarize.completed", groupId = "worker-observer")
    public void onCompleted(SummarizeCompletedEvent evt) {
        log.info("[worker] STATUS: completed jobId={} (summary present: {})",
                evt.getJobId(),
                evt.getResponse() != null);
    }

    @KafkaListener(topics = "medical.summarize.failed", groupId = "worker-observer")
    public void onFailed(SummarizeFailedEvent evt) {
        log.warn("[worker] STATUS: failed jobId={} code={} message={}",
                evt.getJobId(), evt.getCode(), evt.getMessage());
    }
}