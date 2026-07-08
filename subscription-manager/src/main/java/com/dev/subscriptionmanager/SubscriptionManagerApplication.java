package com.dev.subscriptionmanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.amqp.autoconfigure.RabbitAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.jdbc.autoconfigure.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {
		DataSourceAutoConfiguration.class,
		RabbitAutoConfiguration.class
})
public class SubscriptionManagerApplication {

	public static void main(String[] args) {
		SpringApplication.run(SubscriptionManagerApplication.class, args);
	}

}