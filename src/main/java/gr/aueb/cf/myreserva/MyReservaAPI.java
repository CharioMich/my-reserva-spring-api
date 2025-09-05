package gr.aueb.cf.myreserva;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class MyReservaAPI {

	public static void main(String[] args) {
		SpringApplication.run(MyReservaAPI.class, args);
	}

}
