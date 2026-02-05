package com.example.dataselfie.GestionActividades;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.example.dataselfie.R;

public class RegistrarAvanceActivity extends AppCompatActivity {

    // ESTAS VARIABLES DEBEN IR AQUÍ (Fuera del onCreate)
    private GestionActividades actividad;
    private int posicion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_avance);

        // 1. Obtener la posición enviada desde la lista
        posicion = getIntent().getIntExtra("EXTRA_POSICION", -1);
        actividad = RepositorioActividades.getInstance().getPorId(posicion);

        if (actividad == null) {
            Toast.makeText(this, "Error: Actividad no encontrada", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // 2. Vincular vistas del diseño
        TextView tvId = findViewById(R.id.etIdActividad);
        TextView tvNombre = findViewById(R.id.etNombreActividad);
        TextView tvAvanceActual = findViewById(R.id.etAvanceActual);
        EditText etNuevoAvance = findViewById(R.id.etNuevoAvance);
        Button btnGuardar = findViewById(R.id.btnGuardar);
        Button btnCancelar = findViewById(R.id.btnCancelar);

        // 3. Cargar datos actuales
        tvId.setText(String.valueOf(posicion + 1));
        tvNombre.setText(actividad.getNombre());
        tvAvanceActual.setText(actividad.getAvance() + "%");

        // 4. Configurar Botón Guardar
        btnGuardar.setOnClickListener(v -> {
            String input = etNuevoAvance.getText().toString();
            if (input.isEmpty()) {
                Toast.makeText(this, "Ingresa un valor", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                double nuevoAvance = Double.parseDouble(input);
                if (nuevoAvance < 0 || nuevoAvance > 100) {
                    Toast.makeText(this, "El avance debe estar entre 0 y 100", Toast.LENGTH_SHORT).show();
                    return;
                }
                showConfirmDialog(nuevoAvance);
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Ingresa un número válido", Toast.LENGTH_SHORT).show();
            }
        });

        // 5. Configurar Botón Cancelar
        btnCancelar.setOnClickListener(v -> finish());
    }

    private void showConfirmDialog(double nuevoAvance) {
        new AlertDialog.Builder(this)
                .setTitle("CONFIRMACIÓN")
                .setMessage("¿Usted está seguro de realizar esta acción?")
                .setPositiveButton("SÍ", (dialog, which) -> {
                    // --- CAMBIOS CLAVE AQUÍ ---

                    // 1. Actualizamos el avance en el objeto real (Repositorio)
                    actividad.setAvance(nuevoAvance);

                    // 2. Notificamos que hubo un cambio exitoso
                    setResult(RESULT_OK);

                    Toast.makeText(this, "Avance actualizado con éxito", Toast.LENGTH_SHORT).show();

                    // 3. Cerramos la actividad
                    finish();
                })
                .setNegativeButton("NO", (dialog, which) -> dialog.dismiss())
                .show();
    }
}