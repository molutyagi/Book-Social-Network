package com.booknetwork.api;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;

import com.booknetwork.api.role.Role;
import com.booknetwork.api.role.RoleRepository;

@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
@EnableAsync
public class BookSocialNetworkApplication {

	public static void main(String[] args) {
		SpringApplication.run(BookSocialNetworkApplication.class, args);
	}

	@Bean
	public CommandLineRunner commandLineRunner(RoleRepository roleRepository) {
		return _ -> {
			if (roleRepository.findByName("USER").isEmpty()) {
				// save a couple of roles
				roleRepository.save(Role.builder().name("USER").build());
				roleRepository.save(Role.builder().name("ADMIN").build());
			}
		};
	}
}
