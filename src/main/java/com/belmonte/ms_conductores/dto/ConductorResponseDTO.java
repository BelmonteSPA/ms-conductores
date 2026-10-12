package com.belmonte.ms_conductores.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConductorResponseDTO {

    private Long id;
    private String nombre;
    private String email;
    private String telefono;
    private String numeroLicencia;
    private String patente;
    private String modeloVehiculo;
    private int capacidadPasajeros;
    private boolean disponible;
    private boolean activo;
}
