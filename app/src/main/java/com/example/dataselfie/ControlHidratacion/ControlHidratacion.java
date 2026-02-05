package com.example.dataselfie.ControlHidratacion;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Esta es la clase "memoria". Aquí se guarda cuánta agua llevas,
 * cuál es tu meta y la lista de todos los vasos que te has tomado
 */
public class ControlHidratacion {
    private int metaDiariaML;
    private List<RegistroAgua> registros;

    public ControlHidratacion() {
        this.metaDiariaML = 2500;
        this.registros = new ArrayList<>();
        // Borramos acumuladoHoy de aquí, ya no lo necesitamos fijo
    }

    public int obtenerAcumuladoPorFecha(LocalDate fecha) {
        int suma = 0;
        for (RegistroAgua r : registros) {
            if (r.getFecha().equals(fecha)) {
                suma += r.getCantidadML();
            }
        }
        return suma;
    }

    // Actualizamos este para que el porcentaje sea por fecha
    public int calcularPorcentajePorFecha(LocalDate fecha) {
        int actual = obtenerAcumuladoPorFecha(fecha);
        if (metaDiariaML == 0) return 0;
        return (actual * 100) / metaDiariaML;
    }

    public List<RegistroAgua> getRegistros() { return registros; }

    public void registrarIngesta(int cantidadML) {
        // Aseguramos que use la fecha de hoy si se registra rápido
        RegistroAgua r = new RegistroAgua(cantidadML, LocalDate.now(), LocalTime.now());
        registros.add(r);
    }

    public void establecerMetaDiaria(int nuevaMeta) {
        this.metaDiariaML = nuevaMeta;
    }

    public int getMetaDiariaML() { return metaDiariaML; }
}
