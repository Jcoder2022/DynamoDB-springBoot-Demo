package com.nectar;

import com.nectar.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DynamoDbSpringBootDemoApplication {

	@Autowired
	private static EmployeeRepository repo;

	public static void main(String[] args) {
		SpringApplication.run(DynamoDbSpringBootDemoApplication.class, args);
		//repo.findByYear("2016");
	}

}
