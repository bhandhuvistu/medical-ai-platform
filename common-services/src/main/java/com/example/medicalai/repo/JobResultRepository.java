package com.example.medicalai.repo;

import com.example.medicalai.domain.JobResult;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
public interface JobResultRepository extends JpaRepository<JobResult, UUID> {}
