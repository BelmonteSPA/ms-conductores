package com.belmonte.ms_conductores.service;

import com.belmonte.ms_conductores.dto.ConductorRequestDTO;
import com.belmonte.ms_conductores.dto.ConductorResponseDTO;
import com.belmonte.ms_conductores.exception.ConductorNoEncontradoException;
import com.belmonte.ms_conductores.exception.DatoDuplicadoException;
import com.belmonte.ms_conductores.model.Conductor;
import com.belmonte.ms_conductores.repository.ConductorRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ConductorService {
    private final ConductorRepository conductorRepository;

    @Autowired
    public ConductorService(ConductorRepository conductorRepository) {
        this.conductorRepository = conductorRepository;
    }

    //Mapeo Entidad -> ResponseDTO
    private ConductorResponseDTO mapToDTO(Conductor conductor) {
        return new ConductorResponseDTO(
                conductor.getIdConductor(),
                conductor.getNombre(),
                conductor.getEmail(),
                conductor.getTelefono(),
                conductor.getNumeroLicencia(),
                conductor.getPatente(),
                conductor.getModeloVehiculo(),
                conductor.getCapacidadPasajeros(),
                conductor.isDisponible(),
                conductor.isActivo()
        );
    }

    //Mapeo RequestDTO -> Entidad (nace activo y disponible)
    private Conductor mapToEntity(ConductorRequestDTO dto) {
        return new Conductor(
                null,
                dto.getNombre(),
                dto.getEmail(),
                dto.getTelefono(),
                dto.getNumeroLicencia(),
                dto.getPatente().toUpperCase(),
                dto.getModeloVehiculo(),
                dto.getCapacidadPasajeros(),
                true,
                true
        );
    }

    private Conductor buscarOFallar(Long id) {
        return conductorRepository
                .findById(id)
                .orElseThrow(() -> new ConductorNoEncontradoException(id));
    }

    //***CRUD***
    //GET /conductores
    public List<ConductorResponseDTO> mostrarConductores() {
        return conductorRepository
                .findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    //GET /conductores/id
    public ConductorResponseDTO mostrarPorId(Long id) {
        return mapToDTO(buscarOFallar(id));
    }

    //POST /conductores
    public ConductorResponseDTO save(ConductorRequestDTO dto) {
        if (conductorRepository.existsByEmail(dto.getEmail())) {
            throw new DatoDuplicadoException("email", dto.getEmail());
        }
        if (conductorRepository.existsByNumeroLicencia(dto.getNumeroLicencia())) {
            throw new DatoDuplicadoException("licencia", dto.getNumeroLicencia());
        }
        if (conductorRepository.existsByPatente(dto.getPatente().toUpperCase())) {
            throw new DatoDuplicadoException("patente", dto.getPatente().toUpperCase());
        }
        Conductor guardado = conductorRepository.save(mapToEntity(dto));
        log.info(">>> Conductor {} registrado correctamente", guardado.getIdConductor());
        return mapToDTO(guardado);
    }

    //PUT /conductores/id
    public ConductorResponseDTO editar(Long id, ConductorRequestDTO dto) {
        Conductor existente = buscarOFallar(id);
        String patente = dto.getPatente().toUpperCase();

        //Si cambia un dato unico, no puede pertenecer a otro conductor
        if (!existente.getEmail().equalsIgnoreCase(dto.getEmail())
                && conductorRepository.existsByEmail(dto.getEmail())) {
            throw new DatoDuplicadoException("email", dto.getEmail());
        }
        if (!existente.getNumeroLicencia().equalsIgnoreCase(dto.getNumeroLicencia())
                && conductorRepository.existsByNumeroLicencia(dto.getNumeroLicencia())) {
            throw new DatoDuplicadoException("licencia", dto.getNumeroLicencia());
        }
        if (!existente.getPatente().equalsIgnoreCase(patente)
                && conductorRepository.existsByPatente(patente)) {
            throw new DatoDuplicadoException("patente", patente);
        }

        existente.setNombre(dto.getNombre());
        existente.setEmail(dto.getEmail());
        existente.setTelefono(dto.getTelefono());
        existente.setNumeroLicencia(dto.getNumeroLicencia());
        existente.setPatente(patente);
        existente.setModeloVehiculo(dto.getModeloVehiculo());
        existente.setCapacidadPasajeros(dto.getCapacidadPasajeros());
        // activo y disponible no se tocan porque se manejan con PATCH
        return mapToDTO(conductorRepository.save(existente));
    }

    //DELETE /conductores/id
    public void borrar(Long id) {
        if (!conductorRepository.existsById(id)) {
            throw new ConductorNoEncontradoException(id);
        }
        conductorRepository.deleteById(id);
    }

    //***EXTRAS***
    //PATCH /conductores/id/estado  (alterna activo <-> inactivo)
    public ConductorResponseDTO cambiarEstado(Long id) {
        Conductor conductor = buscarOFallar(id);
        conductor.setActivo(!conductor.isActivo());
        return mapToDTO(conductorRepository.save(conductor));
    }

    //PATCH /conductores/id/disponibilidad  (alterna disponible <-> ocupado)
    public ConductorResponseDTO cambiarDisponibilidad(Long id) {
        Conductor conductor = buscarOFallar(id);
        conductor.setDisponible(!conductor.isDisponible());
        return mapToDTO(conductorRepository.save(conductor));
    }

    //GET /conductores/id/validar  (lo consume ms-viajes via Feign)
    //devuelve true si el conductor existe, esta activo y esta disponible
    public boolean validarConductor(Long id) {
        Conductor conductor = buscarOFallar(id);
        return conductor.isActivo() && conductor.isDisponible();
    }

    //GET /conductores/activos
    public List<ConductorResponseDTO> mostrarActivos() {
        return conductorRepository
                .findByActivoTrue()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    //GET /conductores/disponibles
    public List<ConductorResponseDTO> mostrarDisponibles() {
        return conductorRepository
                .findByActivoTrueAndDisponibleTrue()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }
}
