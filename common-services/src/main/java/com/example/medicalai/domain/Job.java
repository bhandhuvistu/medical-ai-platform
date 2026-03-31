package com.example.medicalai.domain;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.*;

@Entity
@Table(name = "job")
public class Job {
    @Id
    @Column(columnDefinition = "uuid")
    private UUID id;
    private String patientId;
    @Enumerated(EnumType.STRING)
    private JobStatus status;
    private Instant createdAt;
    private Instant updatedAt;

    public Job(){}
    public Job(UUID id, String patientId, JobStatus status, Instant createdAt, Instant updatedAt){
        this.id=id; this.patientId=patientId; this.status=status; this.createdAt=createdAt; this.updatedAt=updatedAt;
    }
    public UUID getId(){return id;} public void setId(UUID v){this.id=v;}
    public String getPatientId(){return patientId;} public void setPatientId(String v){this.patientId=v;}
    public JobStatus getStatus(){return status;} public void setStatus(JobStatus v){this.status=v;}
    public Instant getCreatedAt(){return createdAt;} public void setCreatedAt(Instant v){this.createdAt=v;}
    public Instant getUpdatedAt(){return updatedAt;} public void setUpdatedAt(Instant v){this.updatedAt=v;}
}
