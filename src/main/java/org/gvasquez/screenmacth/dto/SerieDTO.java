package org.gvasquez.screenmacth.dto;

import org.gvasquez.screenmacth.model.Categoria;

public record SerieDTO(
        String titulo,
        Integer totalDeTemporadas,
        Double evaluacion,
        String poster,
        Categoria genero,
        String actores,
        String sinopsis) {
}
