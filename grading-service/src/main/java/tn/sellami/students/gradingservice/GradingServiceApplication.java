package tn.sellami.students.gradingservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class GradingServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(GradingServiceApplication.class, args);
    }
}
