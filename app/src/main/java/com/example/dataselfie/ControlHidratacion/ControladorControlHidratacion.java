package com.example.dataselfie.ControlHidratacion;

import java.time.LocalDate;
import java.time.LocalTime;

public class ControladorControlHidratacion {
    private ControlHidratacion modelo;

    public ControladorControlHidratacion(ControlHidratacion modelo) {
        this.modelo = modelo;
    }

    public void registrarIngesta(int cantidad) {
        modelo.registrarIngesta(cantidad);
    }

    public void establecerMeta(int nuevaMeta) {
        modelo.establecerMetaDiaria(nuevaMeta);
    }

    public int obtenerProgresoActual(LocalDate fecha) {
        return modelo.obtenerAcumuladoPorFecha(fecha);
    }

    public int obtenerMeta() {
        return modelo.getMetaDiariaML();
    }

    // Metodo para registros con fecha y hora específica
    public void registrarIngestaManual(int cantidad, LocalDate fecha, LocalTime hora) {
        RegistroAgua nuevo = new RegistroAgua(cantidad, fecha, hora);
        modelo.getRegistros().add(nuevo);
    }
}
