package com.example.dataselfie.RegistroSueno;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class DatosPruebaSueno {

    public static final boolean ACTIVAR_DATOS_PRUEBA = false;

    public static final int YEAR = 2026;
    public static final int MES = Calendar.JANUARY;
    public static final int DIA_1 = 17;
    public static final int DIA_2 = 18;

    public static List<DatosSueno> crear() {
        List<DatosSueno> list = new ArrayList<>();

        Calendar c1 = Calendar.getInstance();
        c1.set(YEAR, MES, DIA_1, 0, 0, 0);
        c1.set(Calendar.MILLISECOND, 0);

        Calendar c2 = Calendar.getInstance();
        c2.set(YEAR, MES, DIA_2, 0, 0, 0);
        c2.set(Calendar.MILLISECOND, 0);

        list.add(new DatosSueno(c1.getTime(), "22:30", "06:30", 8.0));
        list.add(new DatosSueno(c2.getTime(), "23:10", "06:40", 7.5));

        return list;
    }
}