package org.gvasquez.screenmacth.principal;

import org.gvasquez.screenmacth.model.DatosEpisodio;
import org.gvasquez.screenmacth.model.DatosSerie;
import org.gvasquez.screenmacth.model.DatosTemporadas;
import org.gvasquez.screenmacth.service.ConsumoApi;
import org.gvasquez.screenmacth.service.ConvierteDatos;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Principal {

    private Scanner sc = new Scanner(System.in);
    private ConsumoApi consumoApi = new ConsumoApi();

    private final String URL_BASE = "https://www.omdbapi.com/?t=";
    private final String API_KEY = "&apikey=4fc7c187";
    private ConvierteDatos conversor = new ConvierteDatos();

    public void muestraElMenu() {
        System.out.print("Escribe el nombre de la erie que desas buscar: ");
        //busca los datos generales de las series
        String nombreSerie = sc.nextLine();
        var json = consumoApi.obtenerDatos(URL_BASE + nombreSerie.replace(" ", "+") + API_KEY);
        var datos = conversor.obtenerDatos(json, DatosSerie.class);
        System.out.println(datos);

        //Buscar los datos de todas las temporadas
        List<DatosTemporadas> temporadas = new ArrayList<>();
        for (int i = 1; i <= datos.totalDeTemporadas(); i++) {
            json = consumoApi.obtenerDatos(URL_BASE + nombreSerie.replace(" ", "+") + "&Season=" + i + API_KEY);
            var datosTemporada = conversor.obtenerDatos(json, DatosTemporadas.class);
            temporadas.add(datosTemporada);
        }
        //temporadas.forEach(System.out::println);

        //mostrar solo el titulo de los episodios para las temporadas
        /*
        for (int i = 0; i < datos.totalDeTemporadas(); i++) {
            List<DatosEpisodio> episodiosTemporadas = temporadas.get(i).episodios();
            for (int j = 0; j < episodiosTemporadas.size(); j++) {
                System.out.println(episodiosTemporadas.get(j).titulo());
            }
        }*/
        temporadas.forEach(t -> t.episodios().forEach(e -> System.out.println(e.titulo())));
       // System.out.println(temporadas);

    }

}//cierre clase
