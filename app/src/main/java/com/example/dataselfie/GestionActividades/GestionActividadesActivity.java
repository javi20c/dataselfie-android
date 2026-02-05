package com.example.dataselfie.GestionActividades;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import com.example.dataselfie.R;

public class GestionActividadesActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ActividadesAdapter adapter;
    private List<GestionActividades> listaCompleta;
    private List<GestionActividades> listaFiltrada;
    private Button btnAgregar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gestion_actividades);

        // 1. Obtener datos (Pasamos 'this' para inicializar el repositorio con SharedPreferences)
        listaCompleta = RepositorioActividades.getInstance(this).getLista();
        listaFiltrada = new ArrayList<>(listaCompleta);

        // 2. Configurar RecyclerView
        recyclerView = findViewById(R.id.recyclerViewActividades);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new ActividadesAdapter(listaFiltrada, new ActividadesAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int posicionReal) {
                Intent intent = new Intent(GestionActividadesActivity.this, DetalleActividadActivity.class);
                intent.putExtra("EXTRA_POSICION", posicionReal);
                startActivity(intent);
            }

            @Override
            public void onPomodoroClick(int position) {}
        });

        recyclerView.setAdapter(adapter);

        // 3. Configurar Botón Agregar
        btnAgregar = findViewById(R.id.btnAgregar);
        btnAgregar.setOnClickListener(v -> {
            Intent intent = new Intent(GestionActividadesActivity.this, CrearActividadActivity.class);
            startActivity(intent);
        });

        // 4. Configurar Spinners
        setupFiltros();
        setupOrdenamiento();
    }

    private void setupFiltros() {
        Spinner spinner = findViewById(R.id.spinnerTipo);
        String[] opciones = {"Filtrar: Todos", "Académica", "Personal"};
        ArrayAdapter<String> adapterSpinner = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, opciones);
        spinner.setAdapter(adapterSpinner);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                filtrarLista(position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void setupOrdenamiento() {
        Spinner spinnerOrden = findViewById(R.id.spinnerOrden);
        String[] opciones = {"Nombre (A-Z)", "Fecha (Más reciente)", "Avance (Descendente)"};
        ArrayAdapter<String> adapterOrden = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, opciones);
        spinnerOrden.setAdapter(adapterOrden);
        spinnerOrden.setSelection(1);

        spinnerOrden.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ordenarLista(position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
        ordenarLista(1);
    }

    private void ordenarLista(int posicion) {
        switch (posicion) {
            case 0:
                Collections.sort(listaFiltrada, (a, b) -> a.getNombre().compareToIgnoreCase(b.getNombre()));
                break;
            case 1:
                Collections.sort(listaFiltrada, (a, b) -> b.getFechaVencimiento().compareTo(a.getFechaVencimiento()));
                break;
            case 2:
                Collections.sort(listaFiltrada, (a, b) -> Double.compare(b.getAvance(), a.getAvance()));
                break;
        }
        adapter.notifyDataSetChanged();
    }

    private void filtrarLista(int opcion) {
        listaFiltrada.clear();
        if (opcion == 0) {
            listaFiltrada.addAll(listaCompleta);
        } else {
            for (GestionActividades act : listaCompleta) {
                if (opcion == 1 && act instanceof ActividadAcademica) listaFiltrada.add(act);
                else if (opcion == 2 && act instanceof ActividadPersonal) listaFiltrada.add(act);
            }
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (adapter != null) {
            // Recargar lista completa del repositorio para ver cambios guardados
            listaCompleta = RepositorioActividades.getInstance(this).getLista();
            filtrarLista(((Spinner)findViewById(R.id.spinnerTipo)).getSelectedItemPosition());
            adapter.notifyDataSetChanged();
        }
    }
}