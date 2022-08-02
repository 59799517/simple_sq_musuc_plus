package com.sqmusicplus;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class SimpleSqMusucPlusApplication {

    public static void main(String[] args) {
        SpringApplication.run(SimpleSqMusucPlusApplication.class, args);
    }

}
