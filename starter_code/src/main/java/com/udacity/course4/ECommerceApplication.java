package com.udacity.course4;

import com.udacity.course4.config.CustomLoggingConfiguration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories("com.udacity.course4.model.persistence.repositories")
@EntityScan("com.udacity.course4.model.persistence")
@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
@ComponentScan(basePackages = {"com.udacity.course4.common", "com.udacity.course4.config", "com.udacity.course4.controllers", "com.udacity.course4.security"})
public class ECommerceApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		System.out.println("Before Configuration");

		//System.setProperty("log4j.configurationFactory", CustomLoggingConfiguration.class.getName());

		System.out.println("After Configuration");

		SpringApplication.run(ECommerceApplication.class, args);
	}

}
