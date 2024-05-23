package org.gvasquez.screenmacth;

import org.gvasquez.screenmacth.model.DatosEpisodio;
import org.gvasquez.screenmacth.model.DatosSerie;
import org.gvasquez.screenmacth.model.DatosTemporadas;
import org.gvasquez.screenmacth.principal.EjemploStreams;
import org.gvasquez.screenmacth.principal.Principal;
import org.gvasquez.screenmacth.repository.SerieRepository;
import org.gvasquez.screenmacth.service.ConsumoApi;
import org.gvasquez.screenmacth.service.ConvierteDatos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class ScreenmacthApplication {

    public static void main(String[] args) {
        SpringApplication.run(ScreenmacthApplication.class, args);
    }
}//cierre clase
