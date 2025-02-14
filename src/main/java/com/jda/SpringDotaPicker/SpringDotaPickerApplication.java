package com.jda.SpringDotaPicker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@PropertySource("classpath:application.properties")
@PropertySource("classpath:database.properties")
public class SpringDotaPickerApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringDotaPickerApplication.class, args);
	}

}
