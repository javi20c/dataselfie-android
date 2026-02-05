package com.example.dataselfie.JuegoMemoria;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class JuegoMemoria {
    private Carta[][] tablero;
    private int paresEncontrados;
    private int intentos;
    private Carta primeraSeleccion;
    private Carta segundaSeleccion;
    private boolean juegoActivo;

    //elementos del juego
    private static final String[] elementosSos = {
            "RECICLAJE", "SOL", "AGUA_LIMPIA", "OCEANO",
            "BICICLETA", "PANEL_SOLAR", "ARBOL", "ECOLOGIA"
    };

    public JuegoMemoria() {
        this.paresEncontrados = 0;
        this.intentos = 0;
        this.juegoActivo = true;
        this.tablero = new Carta[4][4];
        inicializarTablero();
    }

    //GETTERS
    public Carta[][] getTablero() {
        return tablero;
    }

    public int getIntentos() {
        return intentos;
    }

    public int getParesEncontrados() {
        return paresEncontrados;
    }

    public int getTotalPares() {
        return 8;
    }

    private void inicializarTablero() {
        List<String> elementos = new ArrayList<>();


        for (String elemento : elementosSos) {
            elementos.add(elemento);
            elementos.add(elemento);
        }


        Collections.shuffle(elementos);


        int id = 1;
        int posicion = 0;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                tablero[i][j] = new Carta(elementos.get(posicion), id);
                posicion++;
                id++;
            }
        }
    }

    public boolean isJuegoActivo() {
        return juegoActivo;
    }

    public void ocultarCartasNoDescubiertas() {
        if (primeraSeleccion != null && !primeraSeleccion.getDescubierta()) {
            primeraSeleccion.ocultar();
        }
        if (segundaSeleccion != null && !segundaSeleccion.getDescubierta()) {
            segundaSeleccion.ocultar();
        }
    }

    public Carta seleccionarCarta(int idCarta) {
        Carta carta = buscarCartaPorId(idCarta);

        if (carta != null && !carta.getTemporalmenteVisible() && !carta.getDescubierta()) {
            carta.mostrarTemporalmente();
            return carta;
        }

        return null;
    }

    public Carta buscarCartaPorId(int numeroCarta) {
        for (int i = 0; i < 4 ; i++) {
            for (int j = 0; j < 4; j++) {
                Carta cartaActual = tablero[i][j];
                if (cartaActual.getId() == numeroCarta) {
                    return cartaActual;
                }
            }
        }
        return null;
    }

    public void procesarSeleccion(Carta primera, Carta segunda) {
        this.primeraSeleccion = primera;
        this.segundaSeleccion = segunda;
        this.intentos++;

        if (primeraSeleccion.equals(segundaSeleccion)) {
            primeraSeleccion.setDescubierta(true);
            segundaSeleccion.setDescubierta(true);
            paresEncontrados++;
        }
        if (paresEncontrados == 8) {
            juegoActivo = false;
        }
    }

    public boolean esSeleccionValida (int idCarta) {
        Carta carta = buscarCartaPorId(idCarta);
        return carta != null && !carta.getTemporalmenteVisible() && !carta.getDescubierta();
    }

}