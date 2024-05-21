package org.gvasquez.screenmacth;

import org.gvasquez.screenmacth.service.ConsumoApi;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ScreenmacthApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(ScreenmacthApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		System.out.println("Hola desde springboot");
		var consumoApi = new ConsumoApi();
		var json= consumoApi.obtenerDatos("https://www.omdbapi.com/?t=game+of+thrones&apikey=4fc7c187");
		System.out.println(json);
	}
}
