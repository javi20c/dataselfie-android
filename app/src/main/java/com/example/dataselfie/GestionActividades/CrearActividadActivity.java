package com.example.dataselfie.GestionActividades;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.dataselfie.R;

import java.time.LocalDateTime;
import java.util.Calendar;

public class CrearActividadActivity extends AppCompatActivity {

    private Spinner spinCategoria, spinPrioridad, spinTipoAcademico;
    private EditText etNombre, etDescripcion, etAsignatura, etLugar, etTiempo, etFechaHora;
    private LinearLayout layoutTipoAcademico, layoutAsignatura, layoutLugar;
    private Button btnGuardar, btnCancelar;
    private LocalDateTime fechaSeleccionada;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_actividad);

        inicializarVistas();
        configurarSpinners();
        configurarSelectorFecha();

        spinCategoria.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                actualizarFormulario(position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        btnCancelar.setOnClickListener(v -> finish());
        btnGuardar.setOnClickListener(v -> guardarActividad());
    }

    private void inicializarVistas() {
        spinCategoria = findViewById(R.id.spinCategoria);
        spinPrioridad = findViewById(R.id.spinPrioridad);
        spinTipoAcademico = findViewById(R.id.spinTipoAcademico);
        etNombre = findViewById(R.id.etNombre);
        etDescripcion = findViewById(R.id.etDescripcion);
        etAsignatura = findViewById(R.id.etAsignatura);
        etLugar = findViewById(R.id.etLugar);
        etTiempo = findViewById(R.id.etTiempo);
        etFechaHora = findViewById(R.id.etFechaHora);
        layoutTipoAcademico = findViewById(R.id.layoutTipoAcademico);
        layoutAsignatura = findViewById(R.id.layoutAsignatura);
        layoutLugar = findViewById(R.id.layoutLugar);
        btnGuardar = findViewById(R.id.btnGuardar);
        btnCancelar = findViewById(R.id.btnCancelar);
    }

    private void configurarSpinners() {
        String[] categorias = {"Académica", "Personal"};
        spinCategoria.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categorias));
        spinPrioridad.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, Prioridad.values()));
        String[] tipos = {"Clase", "Examen", "Tarea", "Proyecto", "Estudio"};
        spinTipoAcademico.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, tipos));
    }

    private void actualizarFormulario(int posicionCategoria) {
        if (posicionCategoria == 0) { // Académica
            layoutTipoAcademico.setVisibility(View.VISIBLE);
            layoutAsignatura.setVisibility(View.VISIBLE);
            layoutLugar.setVisibility(View.GONE);
        } else { // Personal
            layoutLugar.setVisibility(View.VISIBLE);
            layoutTipoAcademico.setVisibility(View.GONE);
            layoutAsignatura.setVisibility(View.GONE);
        }
    }

    private void configurarSelectorFecha() {
        etFechaHora.setOnClickListener(v -> {
            final Calendar c = Calendar.getInstance();
            new DatePickerDialog(this, (view, y, m, d) -> abrirSelectorHora(y, m, d),
                    c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();
        });
    }

    private void abrirSelectorHora(int year, int month, int day) {
        final Calendar c = Calendar.getInstance();
        new TimePickerDialog(this, (view, h, min) -> {
            fechaSeleccionada = LocalDateTime.of(year, month + 1, day, h, min);
            etFechaHora.setText(fechaSeleccionada.toString().replace("T", " "));
        }, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), true).show();
    }

    private void guardarActividad() {
        if (etNombre.getText().toString().isEmpty() || fechaSeleccionada == null || etTiempo.getText().toString().isEmpty()) {
            Toast.makeText(this, "Por favor complete los campos obligatorios", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            String nombre = etNombre.getText().toString();
            String desc = etDescripcion.getText().toString();
            Prioridad prio = (Prioridad) spinPrioridad.getSelectedItem();
            double tiempo = Double.parseDouble(etTiempo.getText().toString());

            GestionActividades nueva = null; // Inicializada en null

            if (spinCategoria.getSelectedItemPosition() == 0) { // ACADÉMICA
                String tipoAcademico = spinTipoAcademico.getSelectedItem().toString();
                String asig = etAsignatura.getText().toString();

                // Verifica que tu clase ActividadAcademica tenga exactamente estos parámetros:
                nueva = new ActividadAcademica("Académica", nombre, desc, fechaSeleccionada, prio, tiempo, tipoAcademico, asig);
            } else { // PERSONAL
                String lugar = etLugar.getText().toString();

                // Verifica que tu clase ActividadPersonal tenga exactamente estos parámetros:
                nueva = new ActividadPersonal("Personal", nombre, desc, fechaSeleccionada, prio, tiempo, lugar);
            }

            // Solo agregar si 'nueva' no es nula
            if (nueva != null) {
                RepositorioActividades.getInstance().agregarActividad(nueva);
                Toast.makeText(this, "Guardado con éxito", Toast.LENGTH_SHORT).show();
                finish();
            }

        } catch (Exception e) {
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
