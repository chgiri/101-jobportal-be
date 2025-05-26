package com.shekhar.jobportal.service;

import com.shekhar.jobportal.model.Job;
import com.shekhar.jobportal.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JobService {

    private final JobRepository repository;

    @Autowired
    public JobService(JobRepository repository) {
        this.repository = repository;
    }

    public Job createJob(Job job) {
        return repository.save(job); // save(T) method from JPA Repository
    }

    public List<Job> getAllJobs() {
        return repository.findAll();
    }
}
