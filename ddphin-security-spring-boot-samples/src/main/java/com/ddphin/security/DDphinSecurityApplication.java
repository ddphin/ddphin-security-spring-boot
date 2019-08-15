package com.ddphin.security;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement
@SpringBootApplication
public class DDphinSecurityApplication {

    public static void main(String[] args) {
        SpringApplication.run(DDphinSecurityApplication.class, args);
    }
}
