package com.td2.wallet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@SpringBootApplication
public class WalletApplication {

	private static final Logger logger = LoggerFactory.getLogger(WalletApplication.class);

	@Value("${DB_URL}")
	private String jdbcUrl;

	@Value("${DB_USER}")
	private String user;

	@Value("${DB_PASSWORD}")
	private String password;

	public static void main(String[] args) {
		SpringApplication.run(WalletApplication.class, args);
	}

	@Bean
	public CommandLineRunner demo() {
		return (args) -> {
			try (Connection connection = DriverManager.getConnection(jdbcUrl, user, password)) {
				logger.info("Connection to the database established successfully.");
			} catch (SQLException e) {
				logger.error("Error connecting to the database.", e);
			}
		};
	}


}
