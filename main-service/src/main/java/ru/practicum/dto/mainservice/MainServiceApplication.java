package ru.practicum.dto.mainservice;

import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import java.util.TimeZone;

@SpringBootApplication
@ComponentScan(basePackages = {"ru.practicum.dto.mainservice","ru.practicum.client.client"})
public class MainServiceApplication {

    @PostConstruct
    public void init() {
        TimeZone.setDefault(TimeZone.getTimeZone("Europe/Samara"));
    }

    public static void main(String[] args) {
        SpringApplication.run(MainServiceApplication.class, args);
    }

}
