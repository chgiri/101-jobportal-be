package com.shekhar.jobportal.repository;

import com.shekhar.jobportal.model.Job;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobRepository extends JpaRepository<Job, Long> {

}
