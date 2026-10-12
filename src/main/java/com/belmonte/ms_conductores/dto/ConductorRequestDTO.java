package com.belmonte.ms_conductores.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConductorRequestDTO {

    //id no se incluye porque la base de datos lo genera

    //activo y disponible no se incluyen porque se manejan con PATCH

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 150, message = "El nombre no puede superar los 150 caracteres")
    private String nombre;

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El email no tiene un formato valido")
    @Size(max = 150, message = "El email no puede superar los 150 caracteres")
    private String email;

    @Pattern(
            regexp = "^$|^\\+?[0-9]{8,15}$",
            message = "El telefono debe tener entre 8 y 15 digitos (puede iniciar con +)"
    )
    private String telefono;

    @NotBlank(message = "El numero de licencia es obligatorio")
    @Size(max = 20, message = "El numero de licencia no puede superar los 20 caracteres")
    private String numeroLicencia;

    @NotBlank(message = "La patente es obligatoria")
    @Pattern(
            regexp = "^[A-Za-z0-9-]{5,10}$",
            message = "La patente debe tener entre 5 y 10 caracteres (letras, numeros o guion)"
    )
    private String patente;

    @NotBlank(message = "El modelo del vehiculo es obligatorio")
    @Size(max = 100, message = "El modelo no puede superar los 100 caracteres")
    private String modeloVehiculo;

    @NotNull(message = "La capacidad de pasajeros es obligatoria")
    @Min(value = 1, message = "La capacidad minima es 1 pasajero")
    @Max(value = 60, message = "La capacidad maxima es 60 pasajeros")
    private Integer capacidadPasajeros;
}
