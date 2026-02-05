package com.example.dataselfie.RegistroSostenibilidad;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.dataselfie.R;

public class RegistroDiarioActivity extends AppCompatActivity {
    // Etiqueta para mensajes de depuración en Logcat
    private static final String TAG = "SOSTENIBILIDAD";

    // Repositorio para manejar los registros de sostenibilidad
    private RepositorioSostenibilidad repositorio;

    // Checkboxes de acciones sostenibles
    private CheckBox chkMovilidad, chkNoImpresiones, chkNoDescartables, chkRecicle;

    // TextView para mostrar la fecha actual
    private TextView txtFechaHoy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Carga el layout de la actividad
        setContentView(R.layout.activity_registro_diario);

        // Inicializa el repositorio
        repositorio = new RepositorioSostenibilidad(this);

        // Vincula los CheckBox del layout
        chkMovilidad = findViewById(R.id.chkMovilidad);
        chkNoImpresiones = findViewById(R.id.chkNoImpresiones);
        chkNoDescartables = findViewById(R.id.chkNoDescartables);
        chkRecicle = findViewById(R.id.chkRecicle);


        // Vincula el TextView de la fecha
        txtFechaHoy = findViewById(R.id.txtFechaHoy);

        // Vincula los botones
        Button btnGuardar = findViewById(R.id.btnGuardar);
        Button btnCancelar = findViewById(R.id.btnCancelar);

        // Obtiene la fecha actual en formato ISO
        String fechaHoy = UtilFecha.hoyIso();
        txtFechaHoy.setText("(" + fechaHoy + ")");

        // Busca si ya existe un registro para la fecha actual
        RegistroSostenibilidad registroExistente = repositorio.obtenerRegistro(fechaHoy);
        if (registroExistente != null) {

            // Carga los valores guardados en los CheckBox
            chkMovilidad.setChecked(registroExistente.isMovilidad());
            chkNoImpresiones.setChecked(registroExistente.isNoImpresiones());
            chkNoDescartables.setChecked(registroExistente.isNoDescartables());
            chkRecicle.setChecked(registroExistente.isRecicle());

            // Informa que el registro será actualizado
            Toast.makeText(this,
                    "Ya existía un registro de hoy. Al guardar se actualizará la información.",
                    Toast.LENGTH_LONG).show();

            // Registro de depuración
            Log.d(TAG, "Se cargó registro existente para actualizar: " + fechaHoy);

        } else {
            // Registro de depuración cuando no existe registro previo
            Log.d(TAG, "No existía registro para hoy. Se creará uno nuevo: " + fechaHoy);
        }

        // Acción del botón Guardar
        btnGuardar.setOnClickListener(v -> {

            // Verifica si el registro existía antes de guardar
            boolean existiaAntes = (repositorio.obtenerRegistro(fechaHoy) != null);

            // Crea el objeto registro con los valores seleccionados
            RegistroSostenibilidad registro = new RegistroSostenibilidad(
                    fechaHoy,
                    chkMovilidad.isChecked(),
                    chkNoImpresiones.isChecked(),
                    chkNoDescartables.isChecked(),
                    chkRecicle.isChecked()
            );

            // Guarda o actualiza el registro
            repositorio.guardarRegistro(registro);

            // Muestra mensaje según el tipo de operación
            if (existiaAntes) {
                Toast.makeText(this,
                        "Registro actualizado correctamente.",
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this,
                        "Registro guardado correctamente.",
                        Toast.LENGTH_SHORT).show();
            }

            // Registro de depuración con resumen del guardado
            Log.d(TAG, "Guardado/Actualizado registro hoy: " + fechaHoy +
                    " accionesMarcadas=" + registro.contarAccionesMarcadas());

            // Cierra la actividad y vuelve a la anterior
            finish();
        });


        // Acción del botón Cancelar
        btnCancelar.setOnClickListener(v -> finish());
    }


}
