package com.example.medicalai.repo;

import com.example.medicalai.domain.Job;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobRepository extends JpaRepository<Job, java.util.UUID> {
    // custom queries if needed
}