package com.guru2batch.app;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableBatchProcessing
@ComponentScan({"com.guru2batch.config", "com.guru2batch.service", "com.guru2batch.listener", "com.guru2batch.reader", "com.guru2batch.writer",
		"com.guru2batch.processor", "com.guru2batch.controller"})
@EnableAsync
public class SpringBatchApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBatchApplication.class, args);
	}

}
