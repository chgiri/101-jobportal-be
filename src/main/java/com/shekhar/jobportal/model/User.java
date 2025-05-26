package com.shekhar.jobportal.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "users")
public class User {

    /* Constructor for Normal Users - Password is required  */
    public User(String name, String email, String password, AuthProvider authProvider, Role role) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.authProvider = authProvider;
        this.role = role;
    }

    /* Constructor for Google Users - Password not applicable  */
    public User(String name, String email, AuthProvider authProvider, Role role) {
        this.name = name;
        this.email = email;
        this.password = null; // No password for Google Users
        this.authProvider = authProvider;
        this.role = role;
    }

    public enum AuthProvider {
        LOCAL, GOOGLE
    }

    public enum Role {
        ADMIN, APPLICANT
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = true)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AuthProvider authProvider;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

}
