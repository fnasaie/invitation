package com.naqi.invitation;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
@EnableScheduling
public class InvitationApplication {
    public static String VERSION;

	@Value("${build.version:not-known}")
    String version;

	public static void main(String[] args) {
		SpringApplication.run(InvitationApplication.class, args);
	}

	@Bean
    CommandLineRunner lookup(ApplicationContext context) {
        return args -> {
            VERSION = version;
        };
    }

}
