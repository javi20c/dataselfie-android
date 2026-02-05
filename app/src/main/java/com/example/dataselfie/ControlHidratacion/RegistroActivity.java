package com.example.dataselfie.ControlHidratacion;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Calendar;
import com.example.dataselfie.R;

/**
 * Esta ventana es la que se abre para anotar una nueva toma de agua.
 * Te deja elegir la hora exacta con un reloj.
 */
public class RegistroActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hidratacion_registro);

        EditText inputCantidad = findViewById(R.id.inputCantidadRegistro);
        EditText inputHora = findViewById(R.id.inputHoraRegistro);
        Button btnGuardar = findViewById(R.id.btnGuardarRegistro);
        Button btnCancelar = findViewById(R.id.btnCancelarRegistro);

        // Al tocar el campo de hora, se abre un reloj profesional
        inputHora.setOnClickListener(v -> {
            Calendar c = Calendar.getInstance();
            TimePickerDialog tpd = new TimePickerDialog(this, (view, hourOfDay, minute) -> {
                inputHora.setText(String.format("%02d:%02d", hourOfDay, minute));
            }, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), true);
            tpd.show();
        });

        // Cuando le damos a Guardar, recolectamos los datos y los mandamos al MainActivity
        btnGuardar.setOnClickListener(v -> {
            String cant = inputCantidad.getText().toString();
            String hora = inputHora.getText().toString();

            if (!cant.isEmpty() && !hora.isEmpty()) {
                Intent res = new Intent();
                res.putExtra("CANTIDAD", Integer.parseInt(cant));
                res.putExtra("HORA", hora);
                setResult(RESULT_OK, res);
                finish();
            }
        });

        btnCancelar.setOnClickListener(v -> finish());
    }
}
