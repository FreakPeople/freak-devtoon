package yjh.devtoon;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class DevtoonApplication {

    public static void main(String[] args) {
        SpringApplication.run(DevtoonApplication.class, args);
    }

}
