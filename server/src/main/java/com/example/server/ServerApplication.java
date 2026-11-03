package com.example.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;

@SpringBootApplication
public class ServerApplication {

	public static void main(String[] args) {

		var context = SpringApplication.run(ServerApplication.class, args);

		Environment env = context.getEnvironment();

		String port = env.getProperty("server.port");

		System.out.println("Server is running on port: " + port);
		System.out.println("URL: http://localhost:" + port);
	}
}