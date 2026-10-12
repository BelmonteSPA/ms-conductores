package com.belmonte.ms_conductores.config;

import com.belmonte.ms_conductores.model.Conductor;
import com.belmonte.ms_conductores.repository.ConductorRepository;
import lombok.extern.slf4j.Slf4j;
import net.datafaker.Faker;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

/**
 * Carga datos 100% sintéticos (el anexo prohíbe usar datos reales).
 * Solo corre en el perfil "dev" y únicamente si la tabla está vacía.
 */
@Slf4j
@Configuration
@Profile("dev")
public class DataInitializer {

    @Bean
    CommandLineRunner cargarConductores(ConductorRepository repository) {
        return args -> {
            if (repository.count() > 0) {
                return;
            }

            Faker faker = new Faker(Locale.forLanguageTag("es"));
            Set<String> patentes = new HashSet<>();
            List<Conductor> conductores = new ArrayList<>();

            for (int i = 1; i <= 20; i++) {
                //patente unica con formato chileno: 4 letras + 2 numeros
                String patente;
                do {
                    patente = faker.regexify("[BCDFGHJKLPRSTVWXYZ]{4}[0-9]{2}");
                } while (!patentes.add(patente));

                conductores.add(new Conductor(
                        null,
                        faker.name().fullName(),
                        "conductor" + i + "@ejemplo.test",
                        "+5698" + faker.number().digits(7),
                        String.format("LIC-%08d", i),
                        patente,
                        faker.vehicle().makeAndModel(),
                        faker.number().numberBetween(3, 16),
                        true,
                        true
                ));
            }
            repository.saveAll(conductores);
            log.info(">>> {} conductores sintéticos cargados", repository.count());
        };
    }
}
