package com.shekhar.jobportal.controller;

import com.shekhar.jobportal.model.Job;
import com.shekhar.jobportal.service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/jobs")
public class JobController {

    private JobService service;

    @Autowired
    public JobController(JobService service) {
        this.service = service;
    }

    // Only Role: Admin allowed to use this operation
    @PostMapping
    public ResponseEntity<Job> createJob(@RequestBody Job job) {
        return ResponseEntity.ok(service.createJob(job));
    }

    @GetMapping
    public ResponseEntity<List<Job>> getAllJobs() {
        return ResponseEntity.ok(service.getAllJobs());
    }
}
