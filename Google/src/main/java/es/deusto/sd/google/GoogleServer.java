package es.deusto.sd.google;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "es.deusto.sd.google")
public class GoogleServer {

	public static void main(String[] args) {
		SpringApplication.run(GoogleServer.class, args);
	}
}
