package com.example.dataselfie.GestionActividades;

import java.time.LocalDateTime;
public class ActividadAcademica extends GestionActividades {
    protected String asignatura;
    protected String tipoAcademico;

    public ActividadAcademica(String tipo, String nombre, String desc, LocalDateTime fecha, Prioridad prioridad, double tiempoEst, String asignatura, String tipoAcad) {
        super(tipo, nombre, desc, fecha, prioridad, tiempoEst);
        this.asignatura = asignatura;
        this.tipoAcademico = tipoAcad;
    }

    public String getAsignatura() {
        return this.asignatura;
    }

    public String getTipoAcademico() {
        return this.tipoAcademico;
    }
}