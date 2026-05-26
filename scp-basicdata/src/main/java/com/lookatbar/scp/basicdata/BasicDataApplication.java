package com.lookatbar.scp.basicdata;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.lookatbar.scp.basicdata")
public class BasicDataApplication {

    public static void main(String[] args) {
        SpringApplication.run(BasicDataApplication.class, args);
    }
}
