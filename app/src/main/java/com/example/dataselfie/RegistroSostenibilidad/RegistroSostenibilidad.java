package com.example.dataselfie.RegistroSostenibilidad;

public class RegistroSostenibilidad {

    // Fecha del registro en formato ISO (yyyy-MM-dd)
    private String fechaIso;

    // Indicadores de acciones sostenibles realizadas
    private boolean movilidad;
    private boolean noImpresiones;
    private boolean noDescartables;
    private boolean recicle;

    // Constructor que inicializa el registro con sus valores
    public RegistroSostenibilidad(String fechaIso, boolean movilidad, boolean noImpresiones,
                                  boolean noDescartables, boolean recicle) {
        this.fechaIso = fechaIso;
        this.movilidad = movilidad;
        this.noImpresiones = noImpresiones;
        this.noDescartables = noDescartables;
        this.recicle = recicle;
    }

    // Devuelve la fecha del registro
    public String getFechaIso() { return fechaIso; }

    // Indica si se realizó movilidad sostenible
    public boolean isMovilidad() { return movilidad; }

    // Indica si no se realizaron impresiones
    public boolean isNoImpresiones() { return noImpresiones; }

    // Indica si no se usaron productos descartables
    public boolean isNoDescartables() { return noDescartables; }

    // Indica si se realizó reciclaje
    public boolean isRecicle() { return recicle; }

    // Cuenta cuántas acciones sostenibles fueron marcadas
    public int contarAccionesMarcadas() {
        int total = 0;
        if (movilidad) total++;
        if (noImpresiones) total++;
        if (noDescartables) total++;
        if (recicle) total++;
        return total;
    }

}
