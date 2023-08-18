package com.foro.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ApiApplication {

	public static void main(String[] args) {SpringApplication.run(ApiApplication.class, args);}

	/*
	@Bean
	public CommandLineRunner commandLineRunner(
			AuthService service
	) {
		return args -> {
			var admin = new DataRegisterUser("admin", "admin@mail.com", "admin");
;
			System.out.println("Admin token: " + service.register(admin).accessToken());
		};
	}*/

}
