package br.sc.weg.sid;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SidApplication {

	public static void main(String[] args) {
		SpringApplication.run(SidApplication.class, args);

	}

}
