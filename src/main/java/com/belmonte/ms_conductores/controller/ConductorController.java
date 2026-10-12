package com.belmonte.ms_conductores.controller;

import com.belmonte.ms_conductores.dto.ConductorRequestDTO;
import com.belmonte.ms_conductores.dto.ConductorResponseDTO;
import com.belmonte.ms_conductores.service.ConductorService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/conductores")
public class ConductorController {
    private final ConductorService conductorService;

    @Autowired
    public ConductorController(ConductorService conductorService) {
        this.conductorService = conductorService;
    }

    //***CRUD***
    //GET /conductores
    @GetMapping
    public ResponseEntity<List<ConductorResponseDTO>> getTodos() {
        return ResponseEntity.ok(conductorService.mostrarConductores());
    }

    //GET /conductores/id  (404 si no existe)
    @GetMapping("{id}")
    public ResponseEntity<ConductorResponseDTO> getPorId(@PathVariable Long id) {
        return ResponseEntity.ok(conductorService.mostrarPorId(id));
    }

    //POST /conductores  (400 si hay errores de validacion, 409 si email/licencia/patente ya existen)
    @PostMapping
    public ResponseEntity<ConductorResponseDTO> postConductor(@Valid @RequestBody ConductorRequestDTO dto) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(conductorService.save(dto));
    }

    //PUT /conductores/id  (preserva activo y disponible)
    @PutMapping("{id}")
    public ResponseEntity<ConductorResponseDTO> putConductor(
            @PathVariable Long id,
            @Valid @RequestBody ConductorRequestDTO dto
    ) {
        return ResponseEntity.ok(conductorService.editar(id, dto));
    }

    //DELETE /conductores/id
    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteConductor(@PathVariable Long id) {
        conductorService.borrar(id);
        return ResponseEntity.noContent().build();
    }

    //***EXTRAS***
    //PATCH /conductores/id/estado
    //alterna el estado: si esta activo (1) pasa a inactivo (0) y viceversa
    @PatchMapping("{id}/estado")
    public ResponseEntity<ConductorResponseDTO> patchEstado(@PathVariable Long id) {
        return ResponseEntity.ok(conductorService.cambiarEstado(id));
    }

    //PATCH /conductores/id/disponibilidad
    //alterna la disponibilidad: disponible (1) <-> ocupado (0)
    @PatchMapping("{id}/disponibilidad")
    public ResponseEntity<ConductorResponseDTO> patchDisponibilidad(@PathVariable Long id) {
        return ResponseEntity.ok(conductorService.cambiarDisponibilidad(id));
    }

    //GET /conductores/id/validar
    //devuelve true si existe, esta activo y disponible (lo consume ms-viajes via Feign)
    @GetMapping("{id}/validar")
    public ResponseEntity<Boolean> validar(@PathVariable Long id) {
        return ResponseEntity.ok(conductorService.validarConductor(id));
    }

    //GET /conductores/activos
    @GetMapping("activos")
    public ResponseEntity<List<ConductorResponseDTO>> getActivos() {
        return ResponseEntity.ok(conductorService.mostrarActivos());
    }

    //GET /conductores/disponibles
    @GetMapping("disponibles")
    public ResponseEntity<List<ConductorResponseDTO>> getDisponibles() {
        return ResponseEntity.ok(conductorService.mostrarDisponibles());
    }
}
