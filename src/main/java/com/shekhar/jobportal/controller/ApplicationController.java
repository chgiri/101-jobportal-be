package com.shekhar.jobportal.controller;

import com.shekhar.jobportal.model.Application;
import com.shekhar.jobportal.service.ApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/api/applications")
public class ApplicationController {

    private ApplicationService service;

    @Autowired
    public ApplicationController(ApplicationService service) {
        this.service = service;
    }

    @PostMapping("/apply/{jobId}")
    public ResponseEntity<Application> apply(Principal principal, @PathVariable Long jobId) {
        String username = principal.getName();
        return ResponseEntity.ok(service.apply(username, jobId));
    }

}
