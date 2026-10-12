package com.belmonte.ms_conductores.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "conductores")
public class Conductor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_conductor")
    private Long idConductor;

    @Column(nullable = false, length = 150)
    private String nombre;

    @Column(nullable = false, length = 150, unique = true)
    private String email;

    @Column(length = 20)
    private String telefono;

    @Column(name = "numero_licencia", nullable = false, length = 20, unique = true)
    private String numeroLicencia;

    @Column(nullable = false, length = 10, unique = true)
    private String patente;

    @Column(name = "modelo_vehiculo", nullable = false, length = 100)
    private String modeloVehiculo;

    @Column(name = "capacidad_pasajeros", nullable = false)
    private int capacidadPasajeros;

    //true = libre para recibir un viaje (lo cambia ms-viajes al asignar / finalizar)
    @Column(nullable = false)
    private boolean disponible = true;

    //true = conductor habilitado en el sistema
    @Column(nullable = false)
    private boolean activo = true;
}
