package com.hackathon.developerdashboard;

import com.hackathon.developerdashboard.configuration.UserConfigProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;

@SpringBootApplication
public class DeveloperDashboardApplication {

	@Autowired
	private UserConfigProperties userConfigProperties;

	public static void main(String[] args) {
		SpringApplication.run(DeveloperDashboardApplication.class, args);
	}

	@PostConstruct
	public void test() {
		System.out.println("dfdf");
	}

}
