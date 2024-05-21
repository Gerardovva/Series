package org.gvasquez.screenmacth.principal;

import org.gvasquez.screenmacth.model.DatosEpisodio;
import org.gvasquez.screenmacth.model.DatosSerie;
import org.gvasquez.screenmacth.model.DatosTemporadas;
import org.gvasquez.screenmacth.model.Episodio;
import org.gvasquez.screenmacth.service.ConsumoApi;
import org.gvasquez.screenmacth.service.ConvierteDatos;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

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
        //temporadas.forEach(t -> t.episodios().forEach(e -> System.out.println(e.titulo())));
        // System.out.println(temporadas);


        //convertir todas las informaciones a una lista de tipo datosepisodio
        System.out.println("Top 5 episodios");
        List<DatosEpisodio> datosEpisodios = temporadas.stream()
                .flatMap(t -> t.episodios().stream()).collect(Collectors.toList());

        //top 5 episodios
        datosEpisodios.stream()
                .filter(e -> !e.evaluacion().equalsIgnoreCase("N/A"))
                .sorted(Comparator.comparing(DatosEpisodio::evaluacion).reversed())
                .limit(5)
                .forEach(System.out::println);

        //conviertiendo los datos a una lista del tipo episodio
        List<Episodio> episodios = temporadas.stream()
                .flatMap(t -> t.episodios().stream()
                        .map(d -> new Episodio(t.numero(), d)))
                .collect(Collectors.toList());

        episodios.forEach(System.out::println);

        //busqueda de episodios a partir de x año
        System.out.print("Indica el año a partir del cual deseas ver los episodios: ");
        var fecha = sc.nextInt();
        sc.nextLine();

        LocalDate fechaBusqueda = LocalDate.of(fecha, 1, 1);
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MMMM/yyyy");

        episodios.stream()
                .filter(e -> e.getFecha() != null && e.getFecha().isAfter(fechaBusqueda))
                .forEach(e -> {
                    System.out.println("temporada " + e.getTemporada()
                            + " Episodio " + e.getTitulo()
                            + " Fecha de lanzamiento " + e.getFecha().format(dtf));
                });


    }//cierre metodo

}//cierre clase
