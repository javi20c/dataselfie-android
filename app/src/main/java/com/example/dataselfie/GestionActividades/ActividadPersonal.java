package com.example.dataselfie.GestionActividades;

import java.time.LocalDateTime;

public class ActividadPersonal extends GestionActividades {
    protected String lugar;

    public ActividadPersonal(String tipo, String nombre, String desc, LocalDateTime fecha, Prioridad prioridad, double tiempoEst, String lugar) {
        super(tipo, nombre, desc, fecha, prioridad, tiempoEst);
        this.lugar = lugar;
    }

    public String getLugar() {
        return this.lugar;
    }
}
