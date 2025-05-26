package com.shekhar.jobportal.service;

import com.shekhar.jobportal.model.User;
import com.shekhar.jobportal.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository repository;

    @Autowired
    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    public User createUser(User user) {
        User newUser = new User(user.getName(), user.getEmail(), user.getPassword(), user.getAuthProvider(), user.getRole());
        return repository.save(newUser);
    }
}
