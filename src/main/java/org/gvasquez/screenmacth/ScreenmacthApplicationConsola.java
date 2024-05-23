/*package org.gvasquez.screenmacth;

import org.gvasquez.screenmacth.principal.Principal;
import org.gvasquez.screenmacth.repository.SerieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ScreenmacthApplicationConsola implements CommandLineRunner {

    @Autowired
    private SerieRepository repository;

    public static void main(String[] args) {
        SpringApplication.run(ScreenmacthApplicationConsola.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        Principal principal= new Principal(repository);
        principal.muestraElMenu();
    }


}//cierre clase */
