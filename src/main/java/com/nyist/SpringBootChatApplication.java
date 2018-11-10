package com.nyist;

import com.nyist.controller.WebSocket;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@MapperScan("com.nyist.mapper")
@SpringBootApplication
public class SpringBootChatApplication {

	public static void main(String[] args) {
		SpringApplication springApplication = new SpringApplication(SpringBootChatApplication.class);
		ConfigurableApplicationContext configurableApplicationContext = springApplication.run(args);
		WebSocket.setApplicationContext(configurableApplicationContext);
	}
}
