package com.example.dataselfie.GestionActividades;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import com.example.dataselfie.R;

public class EnfoqueTimerActivity extends AppCompatActivity {
    private TextView tvCronometro, tvTitulo;
    private Button btnIniciar;
    private CountDownTimer timer; // Esta es la variable que usamos para el contador
    private long tiempoRestanteMillis = 1500000;
    private boolean isRunning = false;
    private long tiempoOriginalMillis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enfoque_timer);

        tvCronometro = findViewById(R.id.tvCronometro);
        tvTitulo = findViewById(R.id.tvTituloTecnica);
        btnIniciar = findViewById(R.id.btnIniciar);

        Button btn1 = findViewById(R.id.btnOpcion1);
        Button btn2 = findViewById(R.id.btnOpcion2);
        Button btn3 = findViewById(R.id.btnOpcion3);

        String tipo = getIntent().getStringExtra("TIPO_ENFOQUE");

        if ("DEEP_WORK".equals(tipo)) {
            tvTitulo.setText("Sesión Deep Work");
            btn1.setText("45 min");
            btn2.setText("60 min");
            btn3.setText("90 min");
            btn1.setOnClickListener(v -> configurarTiempo(45));
            btn2.setOnClickListener(v -> configurarTiempo(60));
            btn3.setOnClickListener(v -> configurarTiempo(90));
            configurarTiempo(45);
        } else {
            tvTitulo.setText("Sesión Pomodoro");
            btn1.setText("25 min");
            btn2.setText("5 min");
            btn3.setText("15 min");
            btn1.setOnClickListener(v -> configurarTiempo(25));
            btn2.setOnClickListener(v -> configurarTiempo(5));
            btn3.setOnClickListener(v -> configurarTiempo(15));
            configurarTiempo(25);
        }

        btnIniciar.setOnClickListener(v -> {
            if (!isRunning) iniciarCronometro();
        });

        findViewById(R.id.btnPausar).setOnClickListener(v -> {
            if (timer != null) {
                timer.cancel();
                isRunning = false;
            }
        });

        findViewById(R.id.btnAccionFinal).setOnClickListener(v -> {
            if (timer != null) timer.cancel();
            isRunning = false;
            tiempoRestanteMillis = tiempoOriginalMillis;
            actualizarTextoCronometro();
            btnIniciar.setText("Iniciar");
            Toast.makeText(this, "Tiempo restablecido", Toast.LENGTH_SHORT).show();
        });

        findViewById(R.id.btnDebug).setOnClickListener(v -> {
            tvTitulo.setText("MODO PRUEBA (10s)");
            if (timer != null) timer.cancel();
            isRunning = false;
            long diezSegundos = 10 * 1000;
            this.tiempoRestanteMillis = diezSegundos;
            this.tiempoOriginalMillis = diezSegundos;
            actualizarTextoCronometro();
            btnIniciar.setText("INICIAR TEST");
        });

        // --- CORRECCIÓN DEL BOTÓN CERRAR ---
        ImageButton btnCerrar = findViewById(R.id.btnCerrarTimer);
        btnCerrar.setOnClickListener(v -> {
            if (timer != null) {
                timer.cancel(); // Usamos 'timer', no 'countDownTimer'
            }
            finish();
        });

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (timer != null) timer.cancel();
                finish(); // Cierra la actividad
            }
        });
    }

    private void iniciarCronometro() {
        isRunning = true;
        timer = new CountDownTimer(tiempoRestanteMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                tiempoRestanteMillis = millisUntilFinished;
                actualizarTextoCronometro();
            }

            @Override
            public void onFinish() {
                finalizarSesion();
            }
        }.start();
    }

    private void finalizarSesion() {
        isRunning = false;
        int posicion = getIntent().getIntExtra("EXTRA_POSICION", -1);
        String tipoTecnica = getIntent().getStringExtra("TIPO_ENFOQUE");
        GestionActividades actividad = RepositorioActividades.getInstance().getPorId(posicion);

        if (actividad != null) {
            int minutosCompletados = (int) (tiempoOriginalMillis / 60000);
            if (minutosCompletados == 0) minutosCompletados = 1;

            actividad.registrarTiempoEnfoque(tipoTecnica, minutosCompletados);

            android.app.Dialog dialog = new android.app.Dialog(this);
            dialog.setContentView(R.layout.dialog_sesion_finalizada);

            if (dialog.getWindow() != null) {
                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            }

            TextView tvTituloD = dialog.findViewById(R.id.tvTituloSesion);
            TextView tvResumenD = dialog.findViewById(R.id.tvResumenSesion);
            Button btnGuardarD = dialog.findViewById(R.id.btnGuardarSesion);

            tvTituloD.setText("¡Sesión Completada!");
            tvResumenD.setText("Has completado una sesión de " + tipoTecnica +
                    "\nen: " + actividad.getNombre() +
                    "\nTotal: " + minutosCompletados + " min.");

            btnGuardarD.setOnClickListener(v -> {
                dialog.dismiss();
                finish();
            });

            dialog.setCancelable(false);
            dialog.show();
        } else {
            Toast.makeText(this, "Error: No se encontró la actividad", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void actualizarTextoCronometro() {
        int minutos = (int) (tiempoRestanteMillis / 1000) / 60;
        int segundos = (int) (tiempoRestanteMillis / 1000) % 60;
        tvCronometro.setText(String.format("%02d:%02d", minutos, segundos));
    }

    private void configurarTiempo(int minutos) {
        if (timer != null) timer.cancel();
        isRunning = false;
        long millis = (long) minutos * 60 * 1000;
        this.tiempoRestanteMillis = millis;
        this.tiempoOriginalMillis = millis;
        actualizarTextoCronometro();
        btnIniciar.setText("INICIAR");
    }

    // Sugerencia: Detener el timer también si se usa el botón atrás del sistema

}