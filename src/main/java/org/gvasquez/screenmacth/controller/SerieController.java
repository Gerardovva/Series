package org.gvasquez.screenmacth.controller;

import org.gvasquez.screenmacth.dto.EpisodioDTO;
import org.gvasquez.screenmacth.dto.SerieDTO;
import org.gvasquez.screenmacth.repository.SerieRepository;
import org.gvasquez.screenmacth.service.SerieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/series")
public class SerieController {


    @Autowired
    private SerieService servicio;

    @GetMapping()
    public List<SerieDTO> obtenerTodasLasseries() {
        return servicio.obtenerTodasLasseries();
    }


    @GetMapping("/top5")
    public List<SerieDTO> obtenerTop5() {
        return servicio.obtenerTop5();
    }


    @GetMapping("/lanzamientos")
    public List<SerieDTO> obtenerLanzamientosMasrecientes() {
        return servicio.obtenerLanzamientoMasRecientes();
    }

    @GetMapping("/{id}")
    public SerieDTO obtenerPorId(@PathVariable Long id) {
        return servicio.obtenerPorId(id);
    }

    @GetMapping("/{id}/temporadas/todas")
    public List<EpisodioDTO> obtenerTodasLasTemporadas(@PathVariable Long id) {
        return servicio.obtenerTodasLasTemporadas(id);

    }

    @GetMapping("/{id}/temporadas/{numeroTemporada}")
    public List<EpisodioDTO> obtenerTemporadaPorNumero(@PathVariable long id, @PathVariable long numeroTemporada) {
        return  servicio.obtenerTemporadasPorNumero(id,numeroTemporada);
    }


    @GetMapping("/categoria/{nombreGenero}")
    public List<SerieDTO> obtnerSeriePorCategoria(@PathVariable String nombreGenero ){
        return servicio.obtenerSeriePorCategoria(nombreGenero);
    }

}//cierre clase
