package com.example.dataselfie.RegistroSueno;
import android.content.Context;
import android.content.SharedPreferences;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class SuenoStorage {

    private static final String PREFS = "sueno_prefs";
    private static final String KEY = "sleep_records";

    // Guardamos fecha como ddMMyyyy (ej: 17012026)
    private static final SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy", Locale.US);

    public static void save(Context context, List<DatosSueno> records) {
        SharedPreferences sp = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE);

        StringBuilder sb = new StringBuilder();
        for (DatosSueno d : records) {
            // formato: fecha|sleep|wake|hours;
            sb.append(sdf.format(d.getDate())).append("|")
                    .append(d.getSleepTime()).append("|")
                    .append(d.getWakeTime()).append("|")
                    .append(d.getHoursSlept()).append(";");
        }

        sp.edit().putString(KEY, sb.toString()).apply();
    }

    public static List<DatosSueno> load(Context context) {
        SharedPreferences sp = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        String raw = sp.getString(KEY, "");

        List<DatosSueno> list = new ArrayList<>();
        if (raw == null || raw.trim().isEmpty()) return list;

        String[] items = raw.split(";");
        for (String item : items) {
            if (item.trim().isEmpty()) continue;

            String[] parts = item.split("\\|");
            if (parts.length != 4) continue;

            try {
                Date date = sdf.parse(parts[0]);
                String sleep = parts[1];
                String wake = parts[2];
                double hours = Double.parseDouble(parts[3]);

                list.add(new DatosSueno(date, sleep, wake, hours));
            } catch (Exception ignored) {
            }
        }

        return list;
    }

    public static void clear(Context context) {
        SharedPreferences sp = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        sp.edit().remove(KEY).apply();
    }
}