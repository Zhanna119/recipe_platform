package com.example.recipe_platform;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(
		title = "Spring Boot REST API Documentation",
		description = "Spring Boot REST API Documentation",
		contact = @Contact(
				name = "Zanna",
				email = "petraa@inbox.lv"
		)
))
public class RecipePlatformApplication {

	public static void main(String[] args) {
		SpringApplication.run(RecipePlatformApplication.class, args);
	}

}
