package org.mrbag.InfoStorage;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;

@SpringBootApplication
@EnableAutoConfiguration(exclude = RedisAutoConfiguration.class)
public class InfoStorageApplication {

	public static void main(String[] args) {
		SpringApplication.run(InfoStorageApplication.class, args);
	}

}
