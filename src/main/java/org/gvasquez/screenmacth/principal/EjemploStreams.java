package org.gvasquez.screenmacth.principal;

import java.util.Arrays;
import java.util.List;

public class EjemploStreams {
    public void muestraEjemplo() {
        List<String> nombres = Arrays.asList("brenda", "luis", "pedro", "eric", "genesys", "maria");

        nombres.stream()
                .sorted()
                .limit(6)
                .filter(n -> n.startsWith("g"))
                .map(String::toUpperCase)
                .forEach(System.out::println);
    }
}
