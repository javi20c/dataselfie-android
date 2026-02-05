package com.example.dataselfie.AppDataSelfie;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dataselfie.JuegoMemoria.JuegoMemoriaActivity;
import com.example.dataselfie.RegistroSostenibilidad.ResumenSemanalActivity;
import com.example.dataselfie.R;
import com.example.dataselfie.RegistroSueno.RegistroSuenoActivity;
import com.example.dataselfie.ControlHidratacion.AppHidratacionActivity;
import com.example.dataselfie.GestionActividades.GestionActividadesActivity;

import java.util.ArrayList;
import java.util.List;
public class MainActivity extends AppCompatActivity {

    // Lista de pantallas destino según la posición del menú
    private static final Class<?>[] DESTINATIONS = new Class<?>[]{
            GestionActividadesActivity.class, // Gestion de actividades -> reemplazar cuando exista
            AppHidratacionActivity.class,
            RegistroSuenoActivity.class, // Registrar horas de sueño -> reemplazar cuando exista
            ResumenSemanalActivity.class,
            JuegoMemoriaActivity.class, // Juego de memoria -> reemplazar cuando exista
            null  // Salir
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Carga el layout principal
        setContentView(R.layout.activity_main);


        // Configura el RecyclerView del menú
        RecyclerView rvMenu = findViewById(R.id.rvMenu);
        rvMenu.setLayoutManager(new LinearLayoutManager(this));


        // Crea las opciones del menú
        List<MenuOption> options = new ArrayList<>();
        options.add(new MenuOption("Gestión de Actividades", R.drawable.lista_de_verificacion));
        options.add(new MenuOption("Control de hidratación", R.drawable.beber_agua));
        options.add(new MenuOption("Registrar horas de sueño", R.drawable.calidad_de_sueno));
        options.add(new MenuOption("Registro diario de Sostenibilidad", R.drawable.crecimiento));
        options.add(new MenuOption("Juego de memoria", R.drawable.memoria));
        options.add(new MenuOption("Salir", android.R.drawable.ic_lock_power_off));

        // Asigna el adapter y maneja la selección de opciones
        MenuAdapter adapter = new MenuAdapter(options, position -> {

            // Opción Salir
            if (position == 5) {
                showExitDialog();
                return;
            }

            Class<?> target = DESTINATIONS[position];

            // Navega a la pantalla correspondiente
            startActivity(new Intent(MainActivity.this, target));
        });


        // Asigna el adapter al RecyclerView
        rvMenu.setAdapter(adapter);
    }

    // Muestra un diálogo de confirmación para salir de la app
    private void showExitDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Salir")
                .setMessage("¿Deseas cerrar la aplicación?")
                .setPositiveButton("Sí", (dialog, which) -> finishAffinity())
                .setNegativeButton("No", null)
                .show();
    }
}
