package com.belmonte.ms_conductores.exception;

public class ConductorNoEncontradoException extends RuntimeException {

    public ConductorNoEncontradoException(Long id) {
        super("El conductor con ID: " + id + " no existe");
    }
}
