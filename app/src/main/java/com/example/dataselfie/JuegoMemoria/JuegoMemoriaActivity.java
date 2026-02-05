package com.example.dataselfie.JuegoMemoria;

import com.example.dataselfie.R;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class JuegoMemoriaActivity extends AppCompatActivity {

    private JuegoMemoria juego;
    private GridLayout gridTablero;
    private TextView tvEstadisticas;
    private LinearLayout layoutVictoria;

    private ImageButton btnHomeTop, btnReiniciar, btnSalirWin;

    private Button[] botones = new Button[16];
    private Carta primeraSeleccionada = null;
    private Button botonPrimero = null;
    private boolean bloqueoTablero = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_juego_memoria);

        // Vincular vistas
        gridTablero = findViewById(R.id.gridTablero);
        tvEstadisticas = findViewById(R.id.tvEstadisticas);
        layoutVictoria = findViewById(R.id.layoutVictoria);
        btnHomeTop = findViewById(R.id.btnHomeTop);
        btnReiniciar = findViewById(R.id.btnReiniciar);
        btnSalirWin = findViewById(R.id.btnSalirWin);

        // Listeners
        btnHomeTop.setOnClickListener(v -> finish());
        btnSalirWin.setOnClickListener(v -> finish());
        btnReiniciar.setOnClickListener(v -> reiniciarJuego());

        iniciarJuegoNuevo();
    }

    private void iniciarJuegoNuevo() {
        juego = new JuegoMemoria();
        crearTableroEnPantalla();
        actualizarTextoEstadisticas();
        layoutVictoria.setVisibility(View.GONE);
        primeraSeleccionada = null;
        botonPrimero = null;
        bloqueoTablero = false;
    }

    private void reiniciarJuego() {
        Toast.makeText(this, "¡Juego reiniciado!", Toast.LENGTH_SHORT).show();
        iniciarJuegoNuevo();
    }

    private void crearTableroEnPantalla() {
        gridTablero.removeAllViews();

        for (int i = 0; i < 16; i++) {
            CardView card = new CardView(this);
            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.width = 170;
            params.height = 170;
            params.setMargins(10, 10, 10, 10);
            card.setLayoutParams(params);

            card.setRadius(30f);
            card.setCardElevation(8f);

            Button boton = new Button(this);
            boton.setText("");
            boton.setBackgroundResource(R.drawable.interrogacion);

            LinearLayout.LayoutParams btnParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT);
            boton.setLayoutParams(btnParams);

            final int idCarta = i + 1;
            boton.setOnClickListener(v -> gestionarToqueCarta(idCarta, boton));

            botones[i] = boton;
            card.addView(boton);
            gridTablero.addView(card);
        }
    }

    private void gestionarToqueCarta(int id, Button botonTocado) {
        if (bloqueoTablero) return;
        if (!juego.esSeleccionValida(id)) return;

        Carta cartaActual = juego.seleccionarCarta(id);
        actualizarAparienciaBoton(botonTocado, cartaActual);

        // --- 2. AQUÍ ACTIVAMOS LA ANIMACIÓN "GELATINA" ---
        // Buscamos al CardView (que es el padre del botón) para animar todo el bloque
        View cardPadre = (View) botonTocado.getParent();
        animarGelatina(cardPadre);
        // -------------------------------------------------

        if (primeraSeleccionada == null) {
            primeraSeleccionada = cartaActual;
            botonPrimero = botonTocado;
        } else {
            bloqueoTablero = true;
            juego.procesarSeleccion(primeraSeleccionada, cartaActual);
            actualizarTextoEstadisticas();

            if (primeraSeleccionada.equals(cartaActual)) {
                primeraSeleccionada = null;
                botonPrimero = null;
                bloqueoTablero = false;
                verificarFinDeJuego();
            } else {
                new Handler(Looper.getMainLooper()).postDelayed(() -> {
                    juego.ocultarCartasNoDescubiertas();
                    actualizarAparienciaBoton(botonPrimero, primeraSeleccionada);
                    actualizarAparienciaBoton(botonTocado, cartaActual);
                    primeraSeleccionada = null;
                    botonPrimero = null;
                    bloqueoTablero = false;
                }, 1000);
            }
        }
    }

    //ANIMACION
    private void animarGelatina(View vista) {
        vista.setScaleX(0.85f); // Se encoge un poquito
        vista.setScaleY(0.85f);

        vista.animate()
                .scaleX(1.0f) // Vuelve a su tamaño
                .scaleY(1.0f)
                .setDuration(350) // Rápido
                .setInterpolator(new OvershootInterpolator(4.0f)) // El número 4 es la fuerza del rebote
                .start();
    }

    private void actualizarAparienciaBoton(Button btn, Carta carta) {
        if (carta.getDescubierta() || carta.getTemporalmenteVisible()) {
            btn.setText("");
            int idImagen = obtenerImagenResource(carta.getElemento());
            btn.setBackgroundResource(idImagen);
        } else {
            btn.setText("");
            btn.setBackgroundResource(R.drawable.interrogacion);
        }
    }

    private void verificarFinDeJuego() {
        if (!juego.isJuegoActivo()) {
            layoutVictoria.setVisibility(View.VISIBLE);
            for (Button b : botones) {
                b.setEnabled(false);
            }
        }
    }

    private void actualizarTextoEstadisticas() {
        tvEstadisticas.setText("Intentos: " + juego.getIntentos() + " | Pares: " + juego.getParesEncontrados() + "/8");
    }

    private int obtenerImagenResource(String nombreElemento) {
        switch (nombreElemento) {
            case "RECICLAJE": return R.drawable.reciclaje;
            case "SOL": return R.drawable.sol;
            case "AGUA_LIMPIA": return R.drawable.agua;
            case "OCEANO": return R.drawable.oceano;
            case "BICICLETA": return R.drawable.bicicleta;
            case "PANEL_SOLAR": return R.drawable.panel_solar;
            case "ARBOL": return R.drawable.arbol;
            case "ECOLOGIA": return R.drawable.ecologia;
            default: return R.drawable.ic_launcher_foreground;
        }
    }
}