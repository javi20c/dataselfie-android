package com.example.dataselfie.ControlHidratacion;

import android.content.Context;
import android.content.SharedPreferences;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class AguaStorage {
    private static final String PREFS = "agua_prefs";
    private static final String KEY_REGISTROS = "registros_agua";
    private static final String KEY_META = "meta_diaria";

    private static final DateTimeFormatter dFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter tFormatter = DateTimeFormatter.ofPattern("HH:mm");

    // Guarda la lista completa y la meta
    public static void save(Context context, List<RegistroAgua> records, int meta) {
        SharedPreferences sp = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        StringBuilder sb = new StringBuilder();
        for (RegistroAgua r : records) {
            sb.append(r.getFecha().format(dFormatter)).append("|")
                    .append(r.getHora().format(tFormatter)).append("|")
                    .append(r.getCantidadML()).append(";");
        }
        sp.edit().putString(KEY_REGISTROS, sb.toString()).putInt(KEY_META, meta).apply();
    }

    // Carga la lista desde el celular
    public static List<RegistroAgua> load(Context context) {
        SharedPreferences sp = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        String raw = sp.getString(KEY_REGISTROS, "");
        List<RegistroAgua> list = new ArrayList<>();
        if (raw == null || raw.isEmpty()) return list;

        String[] items = raw.split(";");
        for (String item : items) {
            String[] parts = item.split("\\|");
            if (parts.length == 3) {
                list.add(new RegistroAgua(Integer.parseInt(parts[2]),
                        LocalDate.parse(parts[0], dFormatter),
                        LocalTime.parse(parts[1], tFormatter)));
            }
        }
        return list;
    }

    public static int loadMeta(Context context) {
        return context.getSharedPreferences(PREFS, Context.MODE_PRIVATE).getInt(KEY_META, 2500);
    }
}