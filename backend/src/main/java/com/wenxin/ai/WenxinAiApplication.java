package com.wenxin.ai;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@MapperScan("com.wenxin.ai.mapper")
@PropertySource("classpath:application.yml")
public class WenxinAiApplication {

	public static void main(String[] args) {
		SpringApplication.run(WenxinAiApplication.class, args);
	}

}
