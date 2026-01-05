package com.safiye.twitterapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class TwitterApiApplication {

	public static void main(String[] args) {
        //oluşturulan tüm componentlerden otomatik instance oluşturur
        ConfigurableApplicationContext ctx=
        SpringApplication.run(TwitterApiApplication.class, args);
        for (String instanceName : ctx.getBeanDefinitionNames()) {
            System.out.println(instanceName);
        }
	}

}
