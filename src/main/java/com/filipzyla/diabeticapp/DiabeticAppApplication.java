package com.filipzyla.diabeticapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories
@EnableCaching
@SpringBootApplication(exclude = ErrorMvcAutoConfiguration.class)
public class DiabeticAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(DiabeticAppApplication.class, args);
    }

}