package com.filipzyla.diabeticapp;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories
@EnableEncryptableProperties
@SpringBootApplication(exclude = ErrorMvcAutoConfiguration.class)
public class DiabeticAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(DiabeticAppApplication.class, args);
    }

}