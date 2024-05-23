package org.gvasquez.screenmacth.principal;

import org.gvasquez.screenmacth.model.*;
import org.gvasquez.screenmacth.repository.SerieRepository;
import org.gvasquez.screenmacth.service.ConsumoApi;
import org.gvasquez.screenmacth.service.ConvierteDatos;

import java.util.*;
import java.util.stream.Collectors;

public class Principal {

    private Scanner sc = new Scanner(System.in);
    private ConsumoApi consumoApi = new ConsumoApi();
    private ConvierteDatos conversor = new ConvierteDatos();
    private List<DatosSerie> datosSeries = new ArrayList<>();
    private SerieRepository repositorio;
    private List<Serie> series;
    private Optional<Serie>  serieBuscada;

    private final static String URL_BASE = "https://www.omdbapi.com/?t=";
    private final static String API_KEY = "&apikey=4fc7c187";

    public Principal(SerieRepository repository) {
        this.repositorio = repository;
    }


    public void muestraElMenu() {
        var opcion = -1;
        while (opcion != 0) {
            var menu = """
                    1.- Buscar serie
                    2.- Buscar episodio
                    3.- Mostrar series buscadas
                    4.- Buscar series por titulo
                    5.- Top 5 mejores Series
                    6.- Buscar serie por categoría
                    7.- Serie Por temporada y evaluación
                    8.- Buscar episodio por titulo
                    9.- Top 5 episodios por Serie
                
                    0.- Salir
                    """;

            System.out.println(menu);
            opcion = sc.nextByte();
            sc.nextLine();

            switch (opcion) {
                case 1:
                    buscarSerieWeb();
                    break;
                case 2:
                    buscarEpisodioPorSerie();
                    break;
                case 3:
                    mostrarSerieBuscadas();
                    break;
                case 4:
                    buscarSeriesPorTitulo();
                    break;
                case 5:
                    buscarTop5Series();
                    break;
                case 6:
                    buscarSeriePorCategoria();
                    break;
                case 7:
                    filtraSerieTemporadaEvaluacion();
                    break;
                case 8:
                    buscarEpisodiosPorTitulo();
                    break;
                case 9:
                    busacrTop5Episodios();
                    break;
                case 0:
                    System.out.println("Cerrando la aplicación");
                    break;
                default:
                    System.out.println("Opción invalida");
            }
        }
    }



    //busca los datos generales de las series
    private DatosSerie getDatosSerie() {
        System.out.print("Escribe el nombre de la serie que desas buscar: ");
        String nombreSerie = sc.nextLine();
        var json = consumoApi.obtenerDatos(URL_BASE + nombreSerie.replace(" ", "+") + API_KEY);
        System.out.println(json);
        DatosSerie datos = conversor.obtenerDatos(json, DatosSerie.class);
        return datos;
    }

    private void buscarEpisodioPorSerie() {
        mostrarSerieBuscadas();

        System.out.print("Escribe el nombre de la serie que quieres ver el episodio: ");
        var nombreSerie = sc.nextLine();

        Optional<Serie> serie = series.stream()
                .filter(s -> s.getTitulo().toLowerCase().contains(nombreSerie.toLowerCase()))
                .findFirst();

        if (serie.isPresent()) {
            var serieEncontrada = serie.get();
            List<DatosTemporadas> temporadas = new ArrayList<>();

            for (int i = 1; i <= serieEncontrada.getTotalDeTemporadas(); i++) {

                var json = consumoApi.obtenerDatos(URL_BASE + serieEncontrada.getTitulo().replace(" ", "+") + "&season=" + i + API_KEY);
                DatosTemporadas datosTemporada = conversor.obtenerDatos(json, DatosTemporadas.class);
                temporadas.add(datosTemporada);
            }
            temporadas.forEach(System.out::println);

            List<Episodio> episodios = temporadas.stream()
                    .flatMap(d -> d.episodios().stream()
                            .map(e -> new Episodio(d.numero(), e)))
                    .collect(Collectors.toList());


            serieEncontrada.setEpisodios(episodios);
            repositorio.save(serieEncontrada);
        }


       /* DatosSerie datosSerie = getDatosSerie();
        List<DatosTemporadas> temporadas = new ArrayList<>();
        for (int i = 1; i <= datosSerie.totalTemporadas(); i++) {

            var json = consumoApi.obtenerDatos(URL_BASE + datosSerie.titulo().replace(" ", "+") + "&season=" + i + API_KEY);
            DatosTemporadas datosTemporada = conversor.obtenerDatos(json, DatosTemporadas.class);
            temporadas.add(datosTemporada);
        }
        temporadas.forEach(System.out::println);*/

    }

    private void buscarSerieWeb() {
        DatosSerie datos = getDatosSerie();
        // datosSeries.add(datos);
        Serie serie = new Serie(datos);
        repositorio.save(serie);
        System.out.println(datos);

    }

    private void mostrarSerieBuscadas() {
        /*List<Serie> series = new ArrayList<>();
        series = datosSeries.stream()
                .map(d -> new Serie(d))
                .collect(Collectors.toList());*/
        this.series = repositorio.findAll();

        series.stream()
                .sorted(Comparator.comparing(Serie::getGenero))
                .forEach(System.out::println);
    }

    private void buscarSeriesPorTitulo() {
        System.out.println("Escribe el nombre de la serie que deseas buscar: ");
        String nombreSerie = sc.nextLine();

         serieBuscada = repositorio.findByTituloContainsIgnoreCase(nombreSerie);

        if (serieBuscada.isPresent()) {
            System.out.println("La serie buscada es:  " + serieBuscada.get());
        } else {
            System.out.println("Serie no encontrada");
        }

    }

    private void buscarTop5Series() {
        List<Serie> topSeries = repositorio.findTop5ByOrderByEvaluacionDesc();
        topSeries.forEach(s -> System.out.println("Serie: " + s.getTitulo() + ", Evaluacíon: " + s.getEvaluacion()));
    }

    private void buscarSeriePorCategoria() {
        System.out.println("Escriba el genero/categoria de la serie que desea buscar: ");
        String genero = sc.nextLine();
        var categoria = Categoria.fromEspanol(genero);

        List<Serie> seriePorCategoria = repositorio.findByGenero(categoria);
        System.out.println("Las series de la categoria: " + genero);
        seriePorCategoria.forEach(System.out::println);
    }

    private void filtraSerieTemporadaEvaluacion() {
        System.out.println("Filtrar series en cuantas temporadas: ");
        int totalTemporadas = sc.nextInt();

        System.out.println("Con evaluacion a partir de que valor: ");
        double evaluacion = sc.nextDouble();

        sc.nextLine();
        List<Serie> filtroSerie = repositorio.seriesPorTemporadaYEvaluacion(totalTemporadas, evaluacion);
        System.out.println("** Series filtradas ***");
        filtroSerie.forEach(s -> System.out.println(s.getTitulo() + " - Evaluacion " + s.getEvaluacion()));

    }

    private void buscarEpisodiosPorTitulo() {
        System.out.println("Escribe el nombre del episodio que deseas buscar: ");
        var nombreEpisodio = sc.nextLine();

        List<Episodio> episodiosEncontrados = repositorio.episodiosPorNombre(nombreEpisodio);
        episodiosEncontrados.forEach(e -> System.out.printf("Serie: %s Temporada %s Episodio %s Evaluacion %s", e.getSerie(), e.getTemporada()
                , e.getNumeroEpisodio(), e.getEvaluacion()));
    }

    private void busacrTop5Episodios() {
        buscarSeriesPorTitulo();
        if(serieBuscada.isPresent()){
            Serie serie=serieBuscada.get();
            List<Episodio> topEpisodio=repositorio.top5Episodios(serie);
            topEpisodio.forEach(e ->  System.out.printf("Serie: %s - Temporada %s - Episodio %s - Evaluacion %s \n", e.getSerie(), e.getTemporada()
                    , e.getTitulo(), e.getEvaluacion()));

        }
    }



}//cierre clase

/* public void muestraElMenu() {
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

        for (int i = 0; i < datos.totalDeTemporadas(); i++) {
            List<DatosEpisodio> episodiosTemporadas = temporadas.get(i).episodios();
            for (int j = 0; j < episodiosTemporadas.size(); j++) {
                System.out.println(episodiosTemporadas.get(j).titulo());
            }
        }
        //temporadas.forEach(t -> t.episodios().forEach(e -> System.out.println(e.titulo())));
        // System.out.println(temporadas);


        //convertir todas las informaciones a una lista de tipo datosepisodio
        System.out.println("Top 5 episodios");
        List<DatosEpisodio> datosEpisodios = temporadas.stream()
                .flatMap(t -> t.episodios().stream()).collect(Collectors.toList());

        //top 5 episodios
       datosEpisodios.stream()
                .filter(e -> !e.evaluacion().equalsIgnoreCase("N/A"))
                //  .peek(e -> System.out.println("primer filtro (N/A)" +e))
                .sorted(Comparator.comparing(DatosEpisodio::evaluacion).reversed())
                // .peek(e -> System.out.println("ordenacion de mayor a menor" +e))
                .limit(5)
                .map(e -> e.titulo().toUpperCase())
                //  .peek(e -> System.out.println("tercer filtro mayusculas"+e))
                .forEach(System.out::println);


        //conviertiendo los datos a una lista del tipo episodio
        List<Episodio> episodios = temporadas.stream()
                .flatMap(t -> t.episodios().stream()
                        .map(d -> new Episodio(t.numero(), d)))
                .collect(Collectors.toList());

        //episodios.forEach(System.out::println);

        //busqueda de episodios a partir de x año
        System.out.print("Indica el año a partir del cual deseas ver los episodios: ");
        int fecha = sc.nextInt();
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


        //busca episodios por pedazo del titulo
        System.out.print("Escriba el titulo del episodio que desea ver: ");
        String pedazoTitulo=sc.nextLine();

        Optional<Episodio> episodioBuscado = episodios.stream()
                .filter(e -> e.getTitulo().toUpperCase().contains(pedazoTitulo.toUpperCase()))
                .findFirst();
        if (episodioBuscado.isPresent()){
            System.out.println("Episodio encontrado");
            System.out.println("los datos son : "+episodioBuscado.get());
        }else {
            System.out.println("episodio no encontrado");
        }


        Map<Integer,Double> evaluacionesPorTemporada = episodios.stream()
                .filter(e -> e.getEvaluacion()>0.0)
                .collect(Collectors.groupingBy(Episodio::getTemporada,
                        Collectors.averagingDouble(Episodio::getEvaluacion)));
        System.out.println(evaluacionesPorTemporada);
        DoubleSummaryStatistics est=episodios.stream()
                .filter(e-> e.getEvaluacion()>0.0)
                .collect(Collectors.summarizingDouble(Episodio::getEvaluacion));
        System.out.println("Media de las evaluaciones "+est.getAverage());
        System.out.println("Episodio mejor evaluado "+est.getMax());
        System.out.println("Episodio peor evaluado "+est.getMin());


    }*/