package com.example.dataselfie.RegistroSostenibilidad;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class UtilFecha {

    // Formato de fecha en estilo ISO (yyyy-MM-dd)
    private static final SimpleDateFormat formatoIso =
            new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

    // Devuelve la fecha actual en formato ISO
    public static String hoyIso() {
        Calendar cal = Calendar.getInstance();
        return formatoIso.format(cal.getTime());
    }

    // Genera una lista con las fechas de los últimos 7 días en formato ISO
    public static List<String> ultimos7DiasIso() {
        List<String> fechas = new ArrayList<>();
        Calendar cal = Calendar.getInstance();


        // Retrocede 6 días para iniciar el rango
        cal.add(Calendar.DAY_OF_YEAR, -6);

        // Agrega las 7 fechas consecutivas
        for (int i = 0; i < 7; i++) {
            fechas.add(formatoIso.format(cal.getTime()));
            cal.add(Calendar.DAY_OF_YEAR, 1);
        }
        return fechas;
    }

    // Devuelve el rango de fechas de los últimos 7 días en texto
    public static String rangoUltimos7DiasTexto() {
        List<String> fechas = ultimos7DiasIso();
        return "(" + fechas.get(0) + " - " + fechas.get(6) + ")";
    }
}
