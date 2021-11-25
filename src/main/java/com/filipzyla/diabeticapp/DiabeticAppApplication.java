package com.filipzyla.diabeticapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
public class DiabeticAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(DiabeticAppApplication.class, args);
    }

}