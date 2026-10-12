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

    //tipo de vehiculo: modelos de ejemplo y rango de pasajeros (sin contar al conductor)
    private record Vehiculo(String[] modelos, int minPasajeros, int maxPasajeros) {}

    private static final List<Vehiculo> VEHICULOS = List.of(
            //auto: 4 pasajeros
            new Vehiculo(new String[]{
                    "Opel Corsa", "Chevrolet Sail", "Hyundai Accent", "Kia Morning",
                    "Suzuki Swift", "Toyota Yaris", "Nissan Versa", "Mazda 3"
            }, 4, 4),
            //SUV: entre 4 y 6 pasajeros
            new Vehiculo(new String[]{
                    "Toyota RAV4", "Hyundai Tucson", "Kia Sportage", "Chevrolet Captiva",
                    "Nissan X-Trail", "Mazda CX-5", "Suzuki Vitara"
            }, 4, 6),
            //van: entre 6 y 10 pasajeros
            new Vehiculo(new String[]{
                    "Hyundai H1", "Toyota Hiace", "Mercedes-Benz Sprinter",
                    "Peugeot Traveller", "Renault Master", "Ford Transit"
            }, 6, 10)
    );

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

                //el vehiculo define la capacidad: auto 4, SUV entre 4 y 6, van entre 6 y 10
                Vehiculo vehiculo = VEHICULOS.get(faker.number().numberBetween(0, VEHICULOS.size()));
                String modelo = vehiculo.modelos()[faker.number().numberBetween(0, vehiculo.modelos().length)];
                int capacidad = faker.number().numberBetween(vehiculo.minPasajeros(), vehiculo.maxPasajeros() + 1);

                conductores.add(new Conductor(
                        null,
                        faker.name().fullName(),
                        "conductor" + i + "@ejemplo.test",
                        "+5698" + faker.number().digits(7),
                        String.format("LIC-%08d", i),
                        patente,
                        modelo,
                        capacidad,
                        true,
                        true
                ));
            }
            repository.saveAll(conductores);
            log.info(">>> {} conductores sintéticos cargados", repository.count());
        };
    }
}
