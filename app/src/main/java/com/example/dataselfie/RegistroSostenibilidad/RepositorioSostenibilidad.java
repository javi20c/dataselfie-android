package com.example.dataselfie.RegistroSostenibilidad;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class RepositorioSostenibilidad {

    // Nombre del archivo de preferencias
    private static final String NOMBRE_PREF = "pref_sostenibilidad";

    // Clave para saber si ya se cargaron datos iniciales
    private static final String PREF_DATOS_INICIALES = "datos_iniciales_cargados";

    // Etiqueta para mensajes de depuración
    private static final String TAG = "SOSTENIBILIDAD";

    // Objeto SharedPreferences para guardar datos locales
    private SharedPreferences preferencias;

    // Constructor que inicializa las preferencias
    public RepositorioSostenibilidad(Context contexto) {
        preferencias = contexto.getSharedPreferences(NOMBRE_PREF, Context.MODE_PRIVATE);
        crearDatosIniciales();
    }

    // Carga registros de ejemplo
    private void crearDatosIniciales(){
        boolean yaCargados = preferencias.getBoolean(PREF_DATOS_INICIALES, false);
        if (yaCargados) return;

        // Registros iniciales de prueba
        guardarRegistro(new RegistroSostenibilidad("2026-01-17", true, true, false, true));
        guardarRegistro(new RegistroSostenibilidad("2026-01-18", true, true, true, true));

        // Marca que los datos iniciales ya fueron cargados
        preferencias.edit().putBoolean(PREF_DATOS_INICIALES, true).apply();
        Log.d(TAG, "Datos iniciales cargados correctamente.");
    }

    // Guarda o actualiza un registro en preferencias
    public void guardarRegistro(RegistroSostenibilidad registro) {
        String clave = claveRegistro(registro.getFechaIso());
        String valor = convertirRegistroATexto(registro);

        // Verifica si el registro ya existía
        boolean existia = preferencias.contains(clave);

        // Guarda el valor en SharedPreferences
        preferencias.edit().putString(clave, valor).apply();

        // Mensaje de depuración según el tipo de operación
        if (existia) {
            Log.d(TAG, "Registro actualizado: " + registro.getFechaIso() + " -> " + valor);
        } else {
            Log.d(TAG, "Registro guardado: " + registro.getFechaIso() + " -> " + valor);
        }
    }

    // Obtiene un registro según la fecha
    public RegistroSostenibilidad obtenerRegistro(String fechaIso) {
        String clave = claveRegistro(fechaIso);
        String valor = preferencias.getString(clave, null);
        if (valor == null) return null;

        // Convierte el texto guardado en un objeto RegistroSostenibilidad
        return convertirTextoARegistro(fechaIso, valor);
    }


    // Genera la clave única para cada registro
    private String claveRegistro(String fechaIso) {
        return "registro_" + fechaIso;
    }

    // Convierte un registro en texto para guardarlo
    private String convertirRegistroATexto(RegistroSostenibilidad r) {
        return (r.isMovilidad() ? "1" : "0") + "," +
                (r.isNoImpresiones() ? "1" : "0") + "," +
                (r.isNoDescartables() ? "1" : "0") + "," +
                (r.isRecicle() ? "1" : "0");
    }

    // Convierte el texto guardado nuevamente en un registro
    private RegistroSostenibilidad convertirTextoARegistro(String fechaIso, String texto) {
        String[] partes = texto.split(",");

        boolean movilidad = partes.length > 0 && partes[0].equals("1");
        boolean noImpresiones = partes.length > 1 && partes[1].equals("1");
        boolean noDescartables = partes.length > 2 && partes[2].equals("1");
        boolean recicle = partes.length > 3 && partes[3].equals("1");

        return new RegistroSostenibilidad(fechaIso, movilidad, noImpresiones, noDescartables, recicle);
    }
}
