package br.com.jotave.cannabis_calculator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
@EnableCaching
public class CannabisCalculatorApplication {

	public static void main(String[] args) {
		SpringApplication.run(CannabisCalculatorApplication.class, args);
	}

}
