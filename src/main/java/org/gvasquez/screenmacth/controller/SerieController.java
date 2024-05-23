package org.gvasquez.screenmacth.controller;

import org.gvasquez.screenmacth.dto.SerieDTO;
import org.gvasquez.screenmacth.repository.SerieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class SerieController {

    @Autowired
    private SerieRepository repository;


    @GetMapping("/series")
    public List<SerieDTO> obtenerTodasLasseries() {
        return repository.findAll().stream()
                .map(s -> new SerieDTO(s.getTitulo(), s.getTotalDeTemporadas(),
                        s.getEvaluacion(), s.getPoster(), s.getGenero(), s.getActores(), s.getSinopsis())).collect(Collectors.toList());
    }



}
