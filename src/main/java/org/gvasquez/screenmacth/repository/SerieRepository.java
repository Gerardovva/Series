package org.gvasquez.screenmacth.repository;

import org.gvasquez.screenmacth.model.Categoria;
import org.gvasquez.screenmacth.model.Serie;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SerieRepository extends JpaRepository<Serie,Long> {

   Optional<Serie> findByTituloContainsIgnoreCase(String nombreSerie);

   List<Serie> findTop5ByOrderByEvaluacionDesc();

   List<Serie> findByGenero(Categoria categoria);



}
