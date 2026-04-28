package tn.sellami.students.rest_spring_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class RestSpringApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(RestSpringApiApplication.class, args);
	}

}
