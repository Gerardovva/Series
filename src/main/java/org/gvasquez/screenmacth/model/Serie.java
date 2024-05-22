package org.gvasquez.screenmacth.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import jakarta.persistence.*;

import java.util.List;
import java.util.Optional;
import java.util.OptionalDouble;

@Entity
@Table(name="series")
public class Serie {
    //atributos
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String titulo;

    private Integer totalDeTemporadas;

    private Double evaluacion;

    private String poster;

    @Enumerated(EnumType.STRING)
    private Categoria genero;

    private String actores;

    private String sinopsis;

    @Transient
    private List<Episodio> episodios;

    //constructor
    public Serie(){

    }

    public Serie(DatosSerie datosSerie) {
        this.titulo = datosSerie.titulo();
        this.totalDeTemporadas = datosSerie.totalDeTemporadas();
        this.evaluacion = OptionalDouble.of(Double.parseDouble(datosSerie.evaluacion())).orElse(0);
        this.poster = datosSerie.poster();
        try {
            this.genero = Categoria.fromString(datosSerie.genero().split(",")[0].trim());
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        this.actores = datosSerie.actores();
        this.sinopsis = datosSerie.sinopsis();

    }


    //getter and setter

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Integer getTotalDeTemporadas() {
        return totalDeTemporadas;
    }

    public void setTotalDeTemporadas(Integer totalDeTemporadas) {
        this.totalDeTemporadas = totalDeTemporadas;
    }

    public Double getEvaluacion() {
        return evaluacion;
    }

    public void setEvaluacion(Double evaluacion) {
        this.evaluacion = evaluacion;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public Categoria getGenero() {
        return genero;
    }

    public void setGenero(Categoria genero) {
        this.genero = genero;
    }

    public String getActores() {
        return actores;
    }

    public void setActores(String actores) {
        this.actores = actores;
    }

    public String getSinopsis() {
        return sinopsis;
    }

    public void setSinopsis(String sinopsis) {
        this.sinopsis = sinopsis;
    }

    @Override
    public String toString() {
        return "\t\n******** Resultados ********\n\n"+
                "Genero = " + genero +"\n"+
                "Titulo = " + titulo +"\n"+
                "Total De Temporadas = " + totalDeTemporadas +"\n"+
                "Evaluación = " + evaluacion +"\n"+
                "Poster = " + poster +"\n"+
                "Actores = " + actores +"\n"+
                "Sinopsis = " + sinopsis +"\t\n\n************************** \n\n";
    }
}