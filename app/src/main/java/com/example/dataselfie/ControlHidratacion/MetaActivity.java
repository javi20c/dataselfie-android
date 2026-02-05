package com.example.dataselfie.ControlHidratacion;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.dataselfie.R;

/**
 * Esta es la ventana donde el usuario elige cuánta agua quiere tomar al día.
 */
public class MetaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hidratacion_meta);

        // Referencias a los textos y botones de la pantalla
        TextView txtMetaActual = findViewById(R.id.txtMetaActual);
        EditText inputNuevaMeta = findViewById(R.id.inputNuevaMeta);
        Button btnGuardar = findViewById(R.id.btnGuardarMeta);
        Button btnCancelar = findViewById(R.id.btnCancelarMeta);

        // Recibimos la meta que nos mandó la pantalla principal
        int metaActual = getIntent().getIntExtra("META_ACTUAL", 2500);
        txtMetaActual.setText(metaActual + " ml");

        // Al darle a Guardar, mandamos el nuevo número de vuelta
        btnGuardar.setOnClickListener(v -> {
            String nuevaMetaStr = inputNuevaMeta.getText().toString();
            if (!nuevaMetaStr.isEmpty()) {
                int nuevaMeta = Integer.parseInt(nuevaMetaStr);

                // Preparamos el "paquete" (Intent) con el nuevo valor
                Intent resultado = new Intent();
                resultado.putExtra("NUEVA_META", nuevaMeta);
                // Avisamos que todo salió bien (RESULT_OK) y entregamos el paquete
                setResult(RESULT_OK, resultado);
                finish();
            }
        });

        btnCancelar.setOnClickListener(v -> finish());
    }
}
