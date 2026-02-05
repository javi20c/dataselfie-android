package com.example.dataselfie.GestionActividades;

import android.content.Context;
import android.content.SharedPreferences;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class RepositorioActividades {
    private static RepositorioActividades instance;
    private List<GestionActividades> listaActividades;
    private SharedPreferences prefs;
    private static final String PREFS_NAME = "ActividadesPrefs";
    private static final String KEY_COUNT = "actividad_count";
    private static final String PREF_DATOS_INICIALES = "datos_iniciales_v1";

    private RepositorioActividades(Context context) {
        listaActividades = new ArrayList<>();
        prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        cargarDesdePrefs();
        if (!prefs.getBoolean(PREF_DATOS_INICIALES, false)) {
            cargarDatosPrueba();
            prefs.edit().putBoolean(PREF_DATOS_INICIALES, true).apply();
        }
    }

    public static synchronized RepositorioActividades getInstance(Context context) {
        if (instance == null) {
            instance = new RepositorioActividades(context.getApplicationContext());
        }
        return instance;
    }

    // Sobrecarga para cuando ya sabemos que existe la instancia
    public static RepositorioActividades getInstance() {
        return instance;
    }

    public List<GestionActividades> getLista() {
        return listaActividades;
    }

    public GestionActividades getPorId(int index) {
        if (index >= 0 && index < listaActividades.size()) {
            return listaActividades.get(index);
        }
        return null;
    }

    public void agregarActividad(GestionActividades actividad) {
        listaActividades.add(actividad);
        guardarEnPrefs();
    }

    public void eliminarActividad(GestionActividades actividad) {
        listaActividades.remove(actividad);
        guardarEnPrefs();
    }

    public void guardarCambios() {
        guardarEnPrefs();
    }

    private void cargarDatosPrueba() {
        agregarActividad(new ActividadPersonal("PERSONAL", "Cita Médica", "Chequeo general",
                LocalDateTime.of(2025, 1, 20, 10, 0), Prioridad.MEDIA, 60, "Clínica"));

        ActividadAcademica proyecto = new ActividadAcademica("ACADEMICA", "Proyecto Final", "Avance del proyecto",
                LocalDateTime.of(2025, 1, 30, 23, 59), Prioridad.ALTA, 300, "Sistemas", "PROYECTO");
        proyecto.setAvance(70);
        agregarActividad(proyecto);

        agregarActividad(new ActividadAcademica("ACADEMICA", "Tarea de Lectura", "Leer Cap 5",
                LocalDateTime.of(2025, 1, 19, 18, 0), Prioridad.MEDIA, 120, "Historia", "TAREA"));

        agregarActividad(new ActividadAcademica("ACADEMICA", "Examen Parcial", "Temas 1 a 3",
                LocalDateTime.of(2025, 1, 23, 8, 0), Prioridad.ALTA, 90, "Matemáticas", "EXAMEN"));
    }

    private void guardarEnPrefs() {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(KEY_COUNT, listaActividades.size());
        for (int i = 0; i < listaActividades.size(); i++) {
            GestionActividades act = listaActividades.get(i);
            String baseKey = "act_" + i + "_";
            editor.putString(baseKey + "tipo_obj", act instanceof ActividadAcademica ? "ACAD" : "PERS");
            editor.putString(baseKey + "nombre", act.getNombre());
            editor.putString(baseKey + "desc", act.getDescripcion());
            editor.putString(baseKey + "fecha", act.getFechaVencimiento().toString());
            editor.putString(baseKey + "prioridad", act.getPrioridad().name());
            editor.putFloat(baseKey + "estimado", (float) act.getTiempoEstimado());
            editor.putFloat(baseKey + "avance", (float) act.getAvance());

            if (act instanceof ActividadAcademica) {
                editor.putString(baseKey + "asig", ((ActividadAcademica) act).getAsignatura());
                editor.putString(baseKey + "tipoAcad", ((ActividadAcademica) act).getTipoAcademico());
            } else if (act instanceof ActividadPersonal) {
                editor.putString(baseKey + "lugar", ((ActividadPersonal) act).getLugar());
            }
        }
        editor.apply();
    }

    private void cargarDesdePrefs() {
        int count = prefs.getInt(KEY_COUNT, 0);
        listaActividades.clear();
        for (int i = 0; i < count; i++) {
            String baseKey = "act_" + i + "_";
            String tipoObj = prefs.getString(baseKey + "tipo_obj", "");
            String nombre = prefs.getString(baseKey + "nombre", "");
            String desc = prefs.getString(baseKey + "desc", "");
            LocalDateTime fecha = LocalDateTime.parse(prefs.getString(baseKey + "fecha", LocalDateTime.now().toString()));
            Prioridad prioridad = Prioridad.valueOf(prefs.getString(baseKey + "prioridad", "MEDIA"));
            double estimado = prefs.getFloat(baseKey + "estimado", 0);
            double avance = prefs.getFloat(baseKey + "avance", 0);

            GestionActividades act;
            if ("ACAD".equals(tipoObj)) {
                String asig = prefs.getString(baseKey + "asig", "");
                String tipoA = prefs.getString(baseKey + "tipoAcad", "");
                act = new ActividadAcademica("ACADEMICA", nombre, desc, fecha, prioridad, estimado, asig, tipoA);
            } else {
                String lugar = prefs.getString(baseKey + "lugar", "");
                act = new ActividadPersonal("PERSONAL", nombre, desc, fecha, prioridad, estimado, lugar);
            }
            act.setAvance(avance);
            listaActividades.add(act);
        }
    }
}