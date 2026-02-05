package com.example.dataselfie.GestionActividades;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class GestionActividades {
    protected final String tipo;
    protected final String nombre;
    protected final String descripcion;
    protected final LocalDateTime fechaVencimiento;
    protected final Prioridad prioridad;
    protected final double tiempoEstimado;
    protected double avance;

    // Listas para el historial
    private List<String> sesionesEnfoque = new ArrayList<>();
    private final ArrayList<GestionActividades> listaActividades;
    protected ArrayList<SesionTrabajo> historialTiempo = new ArrayList<>();

    // Acumuladores de tiempo real
    private int tiempoRealAcumulado = 0; // En minutos

    public GestionActividades(String tipo, String nombre, String descripcion,
                              LocalDateTime fechaVencimiento, Prioridad prioridad,
                              double tiempoEstimado) {
        this.listaActividades = new ArrayList<>();
        this.tipo = tipo;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.fechaVencimiento = fechaVencimiento;
        this.prioridad = prioridad;
        this.tiempoEstimado = tiempoEstimado;
        this.avance = 0.0;
    }

    public GestionActividades() {
        this("", "", "", LocalDateTime.now(), Prioridad.MEDIA, 0.0);
    }

    // --- MÉTODOS DE REGISTRO DE TIEMPO (CRONÓMETRO) ---

    /**
     * Registra una sesión de enfoque y acumula el tiempo real.
     * Formato compatible con el adaptador de la tabla de detalles.
     */
    public void registrarTiempoEnfoque(String tecnica, int minutos) {
        this.tiempoRealAcumulado += minutos;

        // Formato: "YYYY-MM-DD | Técnica | Minutos"
        String fecha = java.time.LocalDate.now().toString();
        String registro = fecha + " | " + tecnica + " | " + minutos;

        if (this.sesionesEnfoque == null) {
            this.sesionesEnfoque = new ArrayList<>();
        }
        this.sesionesEnfoque.add(registro);
    }

    public List<String> getSesionesEnfoque() {
        if (sesionesEnfoque == null) sesionesEnfoque = new ArrayList<>();
        return sesionesEnfoque;
    }

    public int getTiempoRealAcumulado() {
        return this.tiempoRealAcumulado;
    }

    // --- GETTERS Y SETTERS ---
    public String getTipo() { return this.tipo; }
    public String getNombre() { return this.nombre; }
    public String getDescripcion() { return this.descripcion; }
    public LocalDateTime getFechaVencimiento() { return this.fechaVencimiento; }
    public Prioridad getPrioridad() { return this.prioridad; }
    public double getTiempoEstimado() { return this.tiempoEstimado; }
    public double getAvance() { return this.avance; }

    public void setAvance(double avance) { this.avance = avance; }

    // --- MÉTODOS DE GESTIÓN DE LISTA DE ACTIVIDADES ---

    public void agregarActividad(GestionActividades actividad) {
        this.listaActividades.add(actividad);
    }

    public List<GestionActividades> obtenerTodas() {
        return new ArrayList<>(this.listaActividades);
    }

    public boolean eliminarActividadPorId(int idActividad) {
        if (idActividad >= 1 && idActividad <= this.listaActividades.size()) {
            this.listaActividades.remove(idActividad - 1);
            return true;
        }
        return false;
    }

    public List<GestionActividades> obtenerPendientes() {
        return this.listaActividades.stream()
                .filter(actividad -> actividad.getAvance() < 100.0)
                .collect(Collectors.toList());
    }
}
