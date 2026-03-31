package com.example.medicalai.repo;

import com.example.medicalai.domain.JobFile;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
public interface JobFileRepository extends JpaRepository<JobFile, UUID> {}
