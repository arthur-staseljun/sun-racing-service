package org.sun.racing.sidewalk.donkey;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SideDonkeyApplication {

	public static void main(String[] args) throws InterruptedException {
		SpringApplication.run(SideDonkeyApplication.class, args);
		Thread.currentThread().join();
	}
}
