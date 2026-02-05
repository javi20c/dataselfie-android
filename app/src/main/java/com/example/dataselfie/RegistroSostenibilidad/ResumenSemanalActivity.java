package com.example.dataselfie.RegistroSostenibilidad;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

import androidx.appcompat.app.AppCompatActivity;

import com.example.dataselfie.R;

public class ResumenSemanalActivity extends AppCompatActivity {

    // Repositorio para leer registros guardados
    private RepositorioSostenibilidad repositorio;


    // TextView para mostrar el rango de fechas de los últimos 7 días
    private TextView txtRango;

    // TextViews para mostrar frecuencia por cada acción
    private TextView txtVeces1, txtVeces2, txtVeces3, txtVeces4;

    // TextViews para mostrar el logro por cada acción
    private TextView txtLogro1, txtLogro2, txtLogro3, txtLogro4;

    // TextViews para mostrar estadísticas adicionales
    private TextView txtDiasAlMenosUna, txtDiasCuatroAcciones;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Carga el layout de la pantalla
        setContentView(R.layout.activity_resumen_semanal);


        // Inicializa el repositorio
        repositorio = new RepositorioSostenibilidad(this);

        // Vincula el TextView del rango
        txtRango = findViewById(R.id.txtRango);

        // Vincula TextViews de frecuencia
        txtVeces1 = findViewById(R.id.txtVeces1);
        txtVeces2 = findViewById(R.id.txtVeces2);
        txtVeces3 = findViewById(R.id.txtVeces3);
        txtVeces4 = findViewById(R.id.txtVeces4);

        // Vincula TextViews de logros
        txtLogro1 = findViewById(R.id.txtLogro1);
        txtLogro2 = findViewById(R.id.txtLogro2);
        txtLogro3 = findViewById(R.id.txtLogro3);
        txtLogro4 = findViewById(R.id.txtLogro4);


        // Vincula TextViews de estadísticas
        txtDiasAlMenosUna = findViewById(R.id.txtDiasAlMenosUna);
        txtDiasCuatroAcciones = findViewById(R.id.txtDiasCuatroAcciones);

        // Botón para ir al registro diario
        Button btnRegistrar = findViewById(R.id.btnRegistrar);
        btnRegistrar.setOnClickListener(v -> {
            Intent intent = new Intent(this, RegistroDiarioActivity.class);
            startActivity(intent);
        });


        // Botón para volver al menú
        ImageButton btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> {
            finish(); // Regresa al menú principal (MainActivity)
        });

    }


    @Override
    protected void onResume() {
        super.onResume();
        // Se recarga el resumen cuando se vuelve a esta pantalla
        cargarResumen();
    }

    private void cargarResumen() {
        // Muestra el rango de fechas de los últimos 7 días
        txtRango.setText(UtilFecha.rangoUltimos7DiasTexto());

        // Obtiene las fechas ISO de los últimos 7 días
        List<String> fechas = UtilFecha.ultimos7DiasIso();

        // Contadores de frecuencia por cada acción (cuántos días se hizo)
        int frecuenciaMovilidad = 0;
        int frecuenciaNoImpresiones = 0;
        int frecuenciaNoDescartables = 0;
        int frecuenciaRecicle = 0;

        // Contadores de días con al menos 1 acción y con 4 acciones completas
        int diasConAlMenosUna = 0;
        int diasConCuatro = 0;

        // Recorre cada día y suma estadísticas según lo guardado
        for (String fecha : fechas) {

            // Busca el registro de ese día
            RegistroSostenibilidad r = repositorio.obtenerRegistro(fecha);

            // Valores por defecto si no hay registro
            boolean movilidad = false;
            boolean noImpresiones = false;
            boolean noDescartables = false;
            boolean recicle = false;


            // Si existe registro, se toman los valores guardados
            if (r != null) {
                movilidad = r.isMovilidad();
                noImpresiones = r.isNoImpresiones();
                noDescartables = r.isNoDescartables();
                recicle = r.isRecicle();
            }

            // Aumenta frecuencia por acción si ese día se marcó
            if (movilidad) frecuenciaMovilidad++;
            if (noImpresiones) frecuenciaNoImpresiones++;
            if (noDescartables) frecuenciaNoDescartables++;
            if (recicle) frecuenciaRecicle++;

            // Cuenta cuántas acciones se cumplieron ese día
            int totalDia = 0;
            if (movilidad) totalDia++;
            if (noImpresiones) totalDia++;
            if (noDescartables) totalDia++;
            if (recicle) totalDia++;

            // Contabiliza días con al menos 1 acción y días con 4 acciones
            if (totalDia >= 1) diasConAlMenosUna++;
            if (totalDia == 4) diasConCuatro++;
        }

        // Muestra cuántas veces se realizó cada acción en los últimos 7 días
        txtVeces1.setText(frecuenciaMovilidad + "/7");
        txtVeces2.setText(frecuenciaNoImpresiones + "/7");
        txtVeces3.setText(frecuenciaNoDescartables + "/7");
        txtVeces4.setText(frecuenciaRecicle + "/7");

        // Genera el texto de logro para cada acción
        txtLogro1.setText(obtenerLogro("movilidad", frecuenciaMovilidad));
        txtLogro2.setText(obtenerLogro("noImpresiones", frecuenciaNoImpresiones));
        txtLogro3.setText(obtenerLogro("noDescartables", frecuenciaNoDescartables));
        txtLogro4.setText(obtenerLogro("recicle", frecuenciaRecicle));


        // Calcula porcentajes sobre 7 días
        int porcentajeAlMenosUna = (diasConAlMenosUna * 100) / 7;
        int porcentajeCuatro = (diasConCuatro * 100) / 7;


        // Muestra resumen de días con al menos 1 acción
        txtDiasAlMenosUna.setText("Días con al menos 1 acción sostenible: " +
                diasConAlMenosUna + " de 7 (" + porcentajeAlMenosUna + "%)");

        // Muestra resumen de días con 4 acciones completas
        txtDiasCuatroAcciones.setText("Días con las 4 acciones completas: " +
                diasConCuatro + " de 7 (" + porcentajeCuatro + "%)");


        // Determina el logro obtenido y aplica el color según el resultado
        String logro1 = obtenerLogro("movilidad", frecuenciaMovilidad);
        txtLogro1.setText(logro1);
        aplicarColorLogro(txtLogro1, logro1);

        String logro2 = obtenerLogro("noImpresiones", frecuenciaNoImpresiones);
        txtLogro2.setText(logro2);
        aplicarColorLogro(txtLogro2, logro2);

        String logro3 = obtenerLogro("noDescartables", frecuenciaNoDescartables);
        txtLogro3.setText(logro3);
        aplicarColorLogro(txtLogro3, logro3);

        String logro4 = obtenerLogro("recicle", frecuenciaRecicle);
        txtLogro4.setText(logro4);
        aplicarColorLogro(txtLogro4, logro4);
    }


    // Devuelve el texto del logro según la frecuencia de la acción
    private String obtenerLogro(String tipoAccion, int frecuencia) {
        if (frecuencia == 7) {
            return "Excelente";
        }
        if (tipoAccion.equals("movilidad") && frecuencia >= 5) {
            return "¡Gran Movilidad!";
        }
        if (frecuencia >= 4) {
            return "Muy bien";
        }
        return "Necesita mejorar";
    }

    // Aplica un color al logro según el resultado obtenido
    private void aplicarColorLogro(TextView txtLogro, String textoLogro) {

        if ("Excelente".equals(textoLogro) || "Muy bien".equals(textoLogro)) {
            txtLogro.setBackgroundResource(R.drawable.chip_verde_oscuro);
        }
        else if ("¡Gran Movilidad!".equals(textoLogro)) {
            txtLogro.setBackgroundResource(R.drawable.chip_verde);
        }
        else if ("Necesita mejorar".equals(textoLogro)) {
            txtLogro.setBackgroundResource(R.drawable.chip_naranja);
        }
    }
}
