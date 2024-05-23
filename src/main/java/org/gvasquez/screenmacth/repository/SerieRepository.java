package org.gvasquez.screenmacth.repository;

import org.gvasquez.screenmacth.dto.EpisodioDTO;
import org.gvasquez.screenmacth.model.Categoria;
import org.gvasquez.screenmacth.model.Episodio;
import org.gvasquez.screenmacth.model.Serie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface SerieRepository extends JpaRepository<Serie,Long> {

   Optional<Serie> findByTituloContainsIgnoreCase(String nombreSerie);

   List<Serie> findTop5ByOrderByEvaluacionDesc();

   List<Serie> findByGenero(Categoria categoria);

   //List<Serie> findByTotalTemporadasLessThanEqualsAndEvaluacionGreaterThanEqual(int totalTemporadas,double evaluacion);
   @Query("SELECT s FROM Serie s WHERE s.totalDeTemporadas <= :totalTemporadas AND s.evaluacion >= :evaluacion")
   List<Serie> seriesPorTemporadaYEvaluacion(int totalTemporadas,double evaluacion);

   @Query("SELECT e FROM Serie s JOIN s.episodios e WHERE e.titulo ILIKE %:nombreEspisodio%")
   List<Episodio> episodiosPorNombre(String nombreEspisodio);

   @Query("SELECT e FROM Serie s JOIN s.episodios e WHERE s = :serie ORDER BY e.evaluacion DESC LIMIT 5")
   List<Episodio> top5Episodios(Serie serie);

   @Query("SELECT s FROM Serie s " +"JOIN s.episodios e "+ "GROUP BY s "+"ORDER BY MAX(e.fechaDeLanzamiento) DESC LIMIT 5")
   List<Serie> lanzamientsoMasRecientes();

   @Query("SELECT e FROM Serie s JOIN s.episodios e WHERE s.id = :id AND e.temporada = :numeroTemporada")
   List<Episodio> obtenerTemporadasPorNumero(long id, long numeroTemporada);
}
