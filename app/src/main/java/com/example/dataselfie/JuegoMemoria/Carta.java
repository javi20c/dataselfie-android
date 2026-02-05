package com.example.dataselfie.JuegoMemoria;

import java.util.Objects;

public class Carta {
    private String elemento;
    private boolean descubierta;
    private boolean temporalmenteVisible;
    private int id;

    public Carta(String elemento, int id) {
        this.elemento = elemento;
        this.descubierta = false;
        this.temporalmenteVisible = false;
        this.id = id;
    }

    //getters
    public String getElemento() {
        return elemento;
    }

    public boolean getDescubierta() {
        return descubierta;
    }

    public boolean getTemporalmenteVisible() {
        return temporalmenteVisible;
    }

    public int getId() {
        return id;
    }

    //setters
    public void setDescubierta(boolean descubierta) {
        this.descubierta = descubierta;
    }

    public void setTemporalmenteVisible(boolean temporalmenteVisible) {
        this.temporalmenteVisible = temporalmenteVisible;
    }

    //métodos de acción
    public void mostrarTemporalmente() {
        this.temporalmenteVisible = true;
    }

    public void ocultar() {
        if (!descubierta) {
            this.temporalmenteVisible = false;
        }
    }

    @Override
    public boolean equals(Object objeto) {
        if (this == objeto) {
            return true;
        }
        if (objeto == null || this.getClass() != objeto.getClass()) {
            return false;
        }

        Carta carta = (Carta) objeto;
        return elemento.equals(carta.elemento);
    }
    @Override
    public int hashCode() {
        return Objects.hash(elemento);
    }


}