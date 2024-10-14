package com.apexglobal.test.orderworker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@ComponentScan("com.apexglobal.test")
@EnableFeignClients
@EnableCaching
@EnableMongoRepositories
public class OrderWorkerApplication {

	public static void main(String[] args) {
		SpringApplication.run(OrderWorkerApplication.class, args);
		System.out.println("AQUII");
	}

}
