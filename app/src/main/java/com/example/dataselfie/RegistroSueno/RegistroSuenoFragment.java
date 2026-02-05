package com.example.dataselfie.RegistroSueno;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.example.dataselfie.R;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class RegistroSuenoFragment extends Fragment {

    private SeguimientoSueno viewModel;
    private EditText editSleep, editWake;
    private Button btnRegistrar, btnCancelar;
    private TextView textFecha;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register_sleep, container, false);

        viewModel = new ViewModelProvider(requireActivity()).get(SeguimientoSueno.class);

        // Enlazar vistas con los ids
        editSleep = view.findViewById(R.id.editSleep);
        editWake = view.findViewById(R.id.editWake);
        btnRegistrar = view.findViewById(R.id.btnRegistrar);
        btnCancelar = view.findViewById(R.id.btnCancelar);
        textFecha = view.findViewById(R.id.textFecha);

        //Mostrar fecha de anoche
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, -1); // AYER

        SimpleDateFormat sdfDia = new SimpleDateFormat("EEE", Locale.getDefault());
        SimpleDateFormat sdfFecha = new SimpleDateFormat("d/M", Locale.getDefault());

        String diaSemana = sdfDia.format(calendar.getTime());
        String diaMes = sdfFecha.format(calendar.getTime());

        //Formatear día
        diaSemana = diaSemana.replace(".", "");
        if (diaSemana.length() > 3) {
            diaSemana = diaSemana.substring(0, 3);
        }
        diaSemana = diaSemana.substring(0, 1).toUpperCase() + diaSemana.substring(1);

        textFecha.setText("Registrando sueño de: " + diaSemana + " " + diaMes);

        // Agregar automáticamente ":" después de 2 dígitos
        editSleep.addTextChangedListener(new TimeTextWatcher(editSleep));
        editWake.addTextChangedListener(new TimeTextWatcher(editWake));

        // Botón Registrar
        btnRegistrar.setOnClickListener(v -> registerSleep());

        // Botón Cancelar
        btnCancelar.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager().popBackStack();
        });


        return view;
    }

    private void registerSleep() {
        String sleepTime = editSleep.getText().toString().trim();
        String wakeTime = editWake.getText().toString().trim();

        if (sleepTime.isEmpty() || wakeTime.isEmpty()) {
            editSleep.setError("Ingrese la hora");
            editWake.setError("Ingrese la hora");
            return;
        }

        // Validar formato HH:MM
        if (!isValidTimeFormat(sleepTime) || !isValidTimeFormat(wakeTime)) {
            editSleep.setError("Formato inválido (HH:MM)");
            editWake.setError("Formato inválido (HH:MM)");
            return;
        }

        // Registrar en el ViewModel
        viewModel.addSleepRecord(sleepTime, wakeTime);

        // Limpiar campos
        editSleep.setText("");
        editWake.setText("");

        // Regresar al resumen automáticamente después de 1 segundo
        new android.os.Handler().postDelayed(
                () -> requireActivity().getSupportFragmentManager().popBackStack(),
                1000
        );
    }

    private boolean isValidTimeFormat(String time) {
        return time.matches("^([0-1]?[0-9]|2[0-3]):[0-5][0-9]$");
    }

    // Clase interna para manejar el formato HH:MM automáticamente
    private class TimeTextWatcher implements TextWatcher {
        private EditText editText;
        private boolean isFormatting = false;

        TimeTextWatcher(EditText editText) {
            this.editText = editText;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {}

        @Override
        public void afterTextChanged(Editable s) {
            if (isFormatting) return;

            isFormatting = true;
            String text = s.toString().replace(":", "");

            if (text.length() >= 2) {
                text = text.substring(0, 2) + ":" + text.substring(2);
                editText.setText(text);
                editText.setSelection(Math.min(text.length(), 5));
            }

            isFormatting = false;
        }
    }
}