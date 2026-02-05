package com.example.dataselfie.GestionActividades;

import java.time.LocalDateTime;

public class SesionTrabajo extends GestionActividades {
    private final String fechaSesion;
    private final String tecnicaAplicada;
    private final int duracionMinutos;

    public SesionTrabajo(String fecha, String tecnica, int duracion) {
        super("SesionTrabajo", "Sesion de " + tecnica, "Sesion registrada de tecnica " + tecnica, (LocalDateTime)null, Prioridad.BAJA, 0.0);
        this.fechaSesion = fecha;
        this.tecnicaAplicada = tecnica;
        this.duracionMinutos = duracion;
    }

    public String getFechaSesion() {
        return this.fechaSesion;
    }

    public String getTecnicaAplicada() {
        return this.tecnicaAplicada;
    }

    public int getDuracionMinutos() {
        return this.duracionMinutos;
    }
}
