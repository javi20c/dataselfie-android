package com.example.dataselfie.ControlHidratacion;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.example.dataselfie.R;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.List;

public class AppHidratacionActivity extends AppCompatActivity {

    // Instanciamos el modelo y el controlador
    private ControlHidratacion controlHidratacion;
    private ControladorControlHidratacion controladorHidratacion;

    // Componentes de la interfaz
    private TextView txtProgreso;
    private ProgressBar barraProgreso;
    private TextView txtPorcentaje;
    private TextView txtHistorial;
    private TextView txtMensajeMeta;
    private Button btnSeleccionarFecha;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_hidratacion);

        // Inicializamos lógica
        controlHidratacion = new ControlHidratacion();
        controladorHidratacion = new ControladorControlHidratacion(controlHidratacion);

        // Vinculamos vistas
        txtProgreso = findViewById(R.id.txtProgreso);
        barraProgreso = findViewById(R.id.barraProgreso);
        txtPorcentaje = findViewById(R.id.txtPorcentaje);
        txtHistorial = findViewById(R.id.txtHistorial);
        txtMensajeMeta = findViewById(R.id.txtMensajeMeta);

        Button btnRegistrar = findViewById(R.id.btnRegistrar);
        Button btnMeta = findViewById(R.id.btnMeta);
        Button btnVolver = findViewById(R.id.btnVolver);
        btnSeleccionarFecha = findViewById(R.id.btnSeleccionarFecha);

        inicializarApp();

        // Preparamos la fecha de hoy
        LocalDate fechaHoy = LocalDate.now();
        String hoyFormateado = fechaHoy.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        btnSeleccionarFecha.setText(hoyFormateado);


        // Listeners
        btnSeleccionarFecha.setOnClickListener(v -> mostrarCalendario());

        btnRegistrar.setOnClickListener(v -> {
            Intent intent = new Intent(this, RegistroActivity.class);
            startActivityForResult(intent, 2);
        });

        btnMeta.setOnClickListener(v -> {
            Intent intent = new Intent(this, MetaActivity.class);
            intent.putExtra("META_ACTUAL", controlHidratacion.getMetaDiariaML());
            startActivityForResult(intent, 1);
        });

        btnVolver.setOnClickListener(v -> finish());

        // Actualizar la vista inicial
        actualizarPantalla(LocalDate.now());
    }

    /**
     * Recibimos los datos cuando volvemos de RegistroActivity o MetaActivity
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && data != null) {
            switch (requestCode) {
                case 1: // Volviendo de MetaActivity
                    int nuevaMeta = data.getIntExtra("NUEVA_META", 2500);
                    controlHidratacion.establecerMetaDiaria(nuevaMeta);
                    actualizarPantalla(LocalDate.now()); // <--- Agrega LocalDate.now()
                    break;

                case 2: // volviendo de RegistroActivity
                    int cantidad = data.getIntExtra("CANTIDAD", 0);
                    String horaTexto = data.getStringExtra("HORA");
                    LocalTime horaLocal = LocalTime.parse(horaTexto, DateTimeFormatter.ofPattern("HH:mm"));

                    // Obtenemos la fecha que está escrita en el botón para saber dónde guardar
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                    LocalDate fechaSeleccionada = LocalDate.parse(btnSeleccionarFecha.getText().toString(), formatter);

                    // Guardamos con la fecha seleccionada
                    controladorHidratacion.registrarIngestaManual(cantidad, fechaSeleccionada, horaLocal);

                    // Refrescamos la pantalla con esa misma fecha
                    actualizarPantalla(fechaSeleccionada);
                    break;
            }
        }
    }

    private void agregarAlHistorialConHora(int cantidad, String hora) {
        String nuevoRegistro = "• " + hora + " - Ingesta de " + cantidad + "ml\n";
        String historialActual = txtHistorial.getText().toString();

        if (historialActual.contains("Aún no hay")) {
            txtHistorial.setText(nuevoRegistro);
        } else {
            txtHistorial.setText(nuevoRegistro + historialActual);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void mostrarCalendario() {
        Calendar calendario = Calendar.getInstance();
        int anio = calendario.get(Calendar.YEAR);
        int mes = calendario.get(Calendar.MONTH);
        int dia = calendario.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dpd = new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
            // Creamos el objeto fecha con lo que eligió el usuario
            LocalDate fechaSeleccionada = LocalDate.of(year, month + 1, dayOfMonth);
            String fechaTexto = String.format("%02d/%02d/%d", dayOfMonth, month + 1, year);

            btnSeleccionarFecha.setText(fechaTexto);
            txtHistorial.setText("Historial del " + fechaTexto + ":");

            // Refrescamos la pantalla con los datos de esa fecha
            actualizarPantalla(fechaSeleccionada);
        }, anio, mes, dia);

        dpd.show();
    }

    private void actualizarPantalla(LocalDate fechaALeer) {
        // Calculamos el progreso real de esa fecha
        int actual = controlHidratacion.obtenerAcumuladoPorFecha(fechaALeer);
        int meta = controlHidratacion.getMetaDiariaML();

        //  se actualiza Barra y Textos de progreso
        txtProgreso.setText("Progreso: " + actual + " / " + meta + " ml");
        barraProgreso.setMax(meta);
        barraProgreso.setProgress(actual);

        int porcentaje = (meta > 0) ? (actual * 100) / meta : 0;
        txtPorcentaje.setText(porcentaje + "%");

        // se limpia el historial visual y filtramos los registros de esa fecha
        StringBuilder sb = new StringBuilder();
        String fechaFormateada = fechaALeer.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        sb.append("Historial del ").append(fechaFormateada).append(":\n");

        boolean tieneRegistros = false;
        for (RegistroAgua r : controlHidratacion.getRegistros()) {
            if (r.getFecha().equals(fechaALeer)) {
                sb.append("• ").append(r.getHora().format(DateTimeFormatter.ofPattern("HH:mm")))
                        .append(" -> ").append(r.getCantidadML()).append(" ml\n");
                tieneRegistros = true;
            }
        }

        if (!tieneRegistros) {
            sb.append("Aún no hay registros para este día.");
        }
        txtHistorial.setText(sb.toString());

        // lógica del mensaje de meta
        if (actual >= meta && meta > 0) {
            txtMensajeMeta.setVisibility(View.VISIBLE);

            // Si el usuario tomó exactamente la meta o más
            if (actual > meta) {
                int exceso = actual - meta; // Calculamos la diferencia
                txtMensajeMeta.setText("¡Felicidades! Meta alcanzada y excedida por " + exceso + " ml");
            } else {
                txtMensajeMeta.setText("¡Felicidades! Has alcanzado tu meta");
            }
        } else {
            txtMensajeMeta.setVisibility(View.GONE);
        }

        // guardamos en el archivo
        AguaStorage.save(this, controlHidratacion.getRegistros(), meta);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void inicializarApp() {
        List<RegistroAgua> registrosGuardados = AguaStorage.load(this);
        int metaGuardada = AguaStorage.loadMeta(this);

        // creamos los 2 registros del 19 de ene
        if (registrosGuardados.isEmpty()) {
            controladorHidratacion.registrarIngestaManual(250, LocalDate.of(2026, 1, 19), LocalTime.of(10, 0));
            controladorHidratacion.registrarIngestaManual(500, LocalDate.of(2026, 1, 19), LocalTime.of(15, 30));

            // Guardamos para que la próxima vez ya no esté vacía
            AguaStorage.save(this, controlHidratacion.getRegistros(), metaGuardada);
        } else {
            // Si ya tenía datos, los pasamos a la memoria (ArrayList)
            controlHidratacion.getRegistros().addAll(registrosGuardados);
        }

        controlHidratacion.establecerMetaDiaria(metaGuardada);

        actualizarPantalla(LocalDate.now());
    }
}
