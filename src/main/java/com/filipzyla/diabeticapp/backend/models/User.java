package com.filipzyla.diabeticapp.backend.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@ToString
@Getter
@Setter
@NoArgsConstructor
@Entity
public class User {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false, length = 50)
    private String username;

    @Column(nullable = false, length = 100)
    private String password;

    @Column(nullable = false, length = 50)
    private String email;

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "user")
    private UserSettings settings;

    public User(String username, String pass, String email) {
        this.email = email;
        this.username = username;
        password = pass;
    }
}