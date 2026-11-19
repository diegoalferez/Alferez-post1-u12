package com.ejemplo.miapp;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * Carga datos de ejemplo SOLO en el perfil dev (H2 en memoria),
 * para que GET /api/productos devuelva algo al arrancar sin configurar nada.
 */
@Component
@Profile("dev")
public class DatosIniciales implements CommandLineRunner {

    private final ProductoRepository repository;

    public DatosIniciales(ProductoRepository repository) {
        this.repository = repository;
    }

    @Override
    public void run(String... args) {
        if (repository.count() == 0) {
            repository.save(new Producto("Teclado mecanico", new BigDecimal("120000.00"), 15));
            repository.save(new Producto("Mouse inalambrico", new BigDecimal("55000.00"), 30));
            repository.save(new Producto("Monitor 24 pulgadas", new BigDecimal("480000.00"), 8));
        }
    }
}
