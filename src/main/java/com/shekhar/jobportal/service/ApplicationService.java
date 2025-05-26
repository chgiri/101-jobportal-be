package com.shekhar.jobportal.service;

import com.shekhar.jobportal.model.Application;
import com.shekhar.jobportal.model.Job;
import com.shekhar.jobportal.model.User;
import com.shekhar.jobportal.repository.ApplicationRepository;
import com.shekhar.jobportal.repository.JobRepository;
import com.shekhar.jobportal.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final UserRepository userRepository;
    private final JobRepository jobRepository;

    @Autowired
    public ApplicationService(ApplicationRepository applicationRepository, UserRepository userRepository, JobRepository jobRepository) {
        this.applicationRepository = applicationRepository;
        this.userRepository = userRepository;
        this.jobRepository = jobRepository;
    }

    public Application apply(String username, Long jobId) {
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));

        Application application = new Application(user, job);

        return applicationRepository.save(application);
    }
}
