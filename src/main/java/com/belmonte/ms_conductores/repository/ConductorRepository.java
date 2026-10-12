package com.belmonte.ms_conductores.repository;

import com.belmonte.ms_conductores.model.Conductor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConductorRepository extends JpaRepository<Conductor, Long> {

    boolean existsByEmail(String email);

    boolean existsByNumeroLicencia(String numeroLicencia);

    boolean existsByPatente(String patente);

    List<Conductor> findByActivoTrue();

    //activos y libres para recibir un viaje
    List<Conductor> findByActivoTrueAndDisponibleTrue();
}
