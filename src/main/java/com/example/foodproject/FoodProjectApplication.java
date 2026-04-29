package com.example.foodproject;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.example.foodproject.mapper")
public class FoodProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(FoodProjectApplication.class, args);
    }

}
