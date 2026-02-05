package com.example.dataselfie.RegistroSueno;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class SeguimientoSueno extends AndroidViewModel {

    private final MutableLiveData<List<DatosSueno>> sleepRecords = new MutableLiveData<>();
    private final MutableLiveData<String> message = new MutableLiveData<>();

    public SeguimientoSueno(@NonNull Application application) {
        super(application);

        if (DatosPruebaSueno.ACTIVAR_DATOS_PRUEBA) {
            List<DatosSueno> seed = DatosPruebaSueno.crear();
            sleepRecords.setValue(seed);
            SuenoStorage.save(getApplication(), seed);
            return;
        }

        List<DatosSueno> loaded = SuenoStorage.load(getApplication());
        sleepRecords.setValue(loaded);
    }


    public double[] calculateSleepHours(String sleepTime, String wakeTime) {
        String[] inicio = sleepTime.split(":");
        String[] fin = wakeTime.split(":");

        int horaInicioInt = Integer.parseInt(inicio[0]);
        int minutoInicioInt = Integer.parseInt(inicio[1]);
        int horaFinInt = Integer.parseInt(fin[0]);
        int minutoFinInt = Integer.parseInt(fin[1]);

        int minutosInicio = horaInicioInt * 60 + minutoInicioInt;
        int minutosFin = horaFinInt * 60 + minutoFinInt;

        boolean cruzaMedianoche = false;

        if (minutosFin < minutosInicio) {
            minutosFin += 1440;
            cruzaMedianoche = true;
        }

        int minutosDormidos = minutosFin - minutosInicio;
        double horasDormidas = minutosDormidos / 60.0;

        return new double[]{horasDormidas, minutosDormidos, cruzaMedianoche ? 1 : 0};
    }

    public boolean yaRegistroHoy() {
        List<DatosSueno> records = sleepRecords.getValue();
        if (records == null) return false;

        // Fecha de ayer
        Calendar ayerCal = Calendar.getInstance();
        ayerCal.add(Calendar.DAY_OF_YEAR, -1);
        Date ayer = ayerCal.getTime();

        Calendar recordCal = Calendar.getInstance();
        Calendar ayerCal2 = Calendar.getInstance();
        ayerCal2.setTime(ayer);

        for (DatosSueno record : records) {
            recordCal.setTime(record.getDate());

            if (recordCal.get(Calendar.YEAR) == ayerCal2.get(Calendar.YEAR) &&
                    recordCal.get(Calendar.MONTH) == ayerCal2.get(Calendar.MONTH) &&
                    recordCal.get(Calendar.DAY_OF_MONTH) == ayerCal2.get(Calendar.DAY_OF_MONTH)) {
                return true;
            }
        }
        return false;
    }

    //Se guarda “para mañana”
    public void addSleepRecord(String sleepTime, String wakeTime) {
        if (yaRegistroHoy()) {
            message.setValue("⚠️ Ya registraste tus horas de sueño para anoche.");
            return;
        }

        double[] resultado = calculateSleepHours(sleepTime, wakeTime);
        double horasDormidas = resultado[0];

        // Fecha de ayer
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, -1);
        Date fechaAyer = calendar.getTime();

        DatosSueno nuevoRegistro = new DatosSueno(fechaAyer, sleepTime, wakeTime, horasDormidas);

        List<DatosSueno> current = sleepRecords.getValue();
        if (current == null) current = new ArrayList<>();

        current.add(nuevoRegistro);

        sleepRecords.setValue(current);

        // Guarda en SharedPreferences (persistencia real)
        SuenoStorage.save(getApplication(), current);

        int horas = (int) horasDormidas;
        int minutos = (int) ((horasDormidas - horas) * 60);
        message.setValue(String.format("✅ Registrado: %d horas y %d minutos de sueño", horas, minutos));
    }

    public List<DatosSueno> getUltimos7Dias() {
        List<DatosSueno> records = sleepRecords.getValue();
        if (records == null || records.isEmpty()) return new ArrayList<>();

        List<DatosSueno> ordenados = new ArrayList<>(records);
        ordenados.sort((a, b) -> b.getDate().compareTo(a.getDate()));

        return ordenados.subList(0, Math.min(ordenados.size(), 7));
    }

    public String generarBarraProgreso(double horas) {
        StringBuilder barra = new StringBuilder();
        int horasCompletas = (int) horas;

        for (int i = 0; i < horasCompletas && i < 8; i++) {
            barra.append("▓");
        }

        int horasFaltantes = 8 - horasCompletas;
        for (int i = 0; i < horasFaltantes && i < 8; i++) {
            barra.append("░");
        }

        return barra.toString();
    }

    public static String formatearDiaSemana(Calendar calendar) {
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        switch (dayOfWeek) {
            case Calendar.SUNDAY: return "Dom";
            case Calendar.MONDAY: return "Lun";
            case Calendar.TUESDAY: return "Mar";
            case Calendar.WEDNESDAY: return "Mié";
            case Calendar.THURSDAY: return "Jue";
            case Calendar.FRIDAY: return "Vie";
            case Calendar.SATURDAY: return "Sáb";
            default: return "Día";
        }
    }

    // Getters
    public MutableLiveData<List<DatosSueno>> getSleepRecords() {
        return sleepRecords;
    }

    public MutableLiveData<String> getMessage() {
        return message;
    }

    // (Opcional) borrar todo desde UI si algún día quieres
    public void borrarTodo() {
        List<DatosSueno> vacio = new ArrayList<>();
        sleepRecords.setValue(vacio);
        SuenoStorage.save(getApplication(), vacio);
        message.setValue("🗑️ Registros borrados.");
    }
}