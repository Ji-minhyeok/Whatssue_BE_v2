package GDG.whatssue;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class WhatssueApplication {
	public static void main(String[] args) {
		SpringApplication.run(WhatssueApplication.class, args);
	}

}
