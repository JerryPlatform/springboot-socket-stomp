package com.example.projectj;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class ProjectJApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProjectJApplication.class, args);
    }

}
