package com.jmnt.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {
		"com.jmnt.controllers",
		"com.jmnt.data",
		"com.jmnt.excelparser",
		"com.jmnt.services",
		"com.jmnt.config"
})
public class Application {
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}
