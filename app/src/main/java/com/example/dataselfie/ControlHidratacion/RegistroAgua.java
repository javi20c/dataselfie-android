package com.example.dataselfie.ControlHidratacion;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Esta clase representa una "toma de agua".
 * Guarda el qué (cantidad), el cuándo (fecha) y el a qué hora.
 */
public class RegistroAgua {

    // El contador nos sirve para que cada registro tenga un ID único
    private static int contador = 1;


    private int id;
    private LocalDate fecha;
    private LocalTime hora;
    private int cantidadML;


    // Se usa cuando el usuario elige manualmente fecha y hora
    public RegistroAgua(int cantidadML, LocalDate fecha, LocalTime hora) {
        this.id = contador++;
        this.fecha = fecha;
        this.hora = hora;
        this.cantidadML = cantidadML;
    }

    // Constructor para cuando sabemos el día, pero le ponemos la hora del momento
    @RequiresApi(api = Build.VERSION_CODES.O)
    public RegistroAgua(int cantidadML, LocalDate fecha) {
        this.id = contador++;
        this.fecha = fecha;
        this.hora = LocalTime.now();
        this.cantidadML = cantidadML;
    }


    //Para cuando registras algo al instante (usa fecha y hora actual)
    public RegistroAgua(int cantidadML) {
        this.id = contador++;
        this.fecha = LocalDate.now();
        this.hora = LocalTime.now();
        this.cantidadML = cantidadML;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }


    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }


    public int getCantidadML() { return cantidadML; }
    public void setCantidadML(int cantidadML) { this.cantidadML = cantidadML; }


    public String formatearParaHistorial() {
        return hora.getHour() + ":" +
                String.format("%02d", hora.getMinute()) +
                " -> " + cantidadML + " ml";
    }
    public LocalTime getHora() {
        return hora;
    }


}
