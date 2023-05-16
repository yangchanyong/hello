package com.chanyongyang.hello;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import lombok.Data;

@SpringBootApplication // 이 어노테이션이 달린 클래스가 있는 패키지를 베이스패키지로 간주한다.
@Data
public class HelloApplication {

	public static void main(String[] args) {
		SpringApplication.run(HelloApplication.class, args);
	}

}
