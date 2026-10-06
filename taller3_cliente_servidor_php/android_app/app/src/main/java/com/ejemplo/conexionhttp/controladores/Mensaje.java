package com.ejemplo.conexionhttp.controladores;

import java.io.Serializable;

/**
 * Clase auxiliar para mapear respuestas JSON tipo {"mensaje": "..."}
 */
public class Mensaje implements Serializable {
    private String mensaje;

    public Mensaje() {
    }

    public Mensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }
}
