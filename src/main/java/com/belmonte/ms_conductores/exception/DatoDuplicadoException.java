package com.belmonte.ms_conductores.exception;

//se usa para email, numero de licencia y patente repetidos
public class DatoDuplicadoException extends RuntimeException {

    public DatoDuplicadoException(String campo, String valor) {
        super("Ya existe un conductor registrado con " + campo + ": " + valor);
    }
}
