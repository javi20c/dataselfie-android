package com.example.dataselfie.GestionActividades;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import com.example.dataselfie.R;

public class DetalleActividadActivity extends AppCompatActivity {

    private GestionActividades actividad;
    private RecyclerView rvHistorial;
    private TextView detAvance, detTiempo, detEstado; // Variables globales para actualizar en onResume

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles);

        int index = getIntent().getIntExtra("EXTRA_POSICION", -1);
        if (index == -1) { finish(); return; }

        actividad = RepositorioActividades.getInstance().getPorId(index);

        // 1. Vincular vistas con los IDs EXACTOS de tu XML
        TextView tvNombre = findViewById(R.id.detNombre);
        TextView tvTipo = findViewById(R.id.detTipo);
        TextView tvAsignatura = findViewById(R.id.detAsignatura);
        TextView tvLugar = findViewById(R.id.detLugar);
        TextView detPrioridad = findViewById(R.id.detPrioridad);
        TextView detFecha = findViewById(R.id.detFecha);

        detEstado = findViewById(R.id.detEstado);
        detAvance = findViewById(R.id.detAvance);
        detTiempo = findViewById(R.id.detTiempo); // Este es el que usaremos para Estimado y Real
        rvHistorial = findViewById(R.id.rvHistorial);

        // 2. Llenar datos fijos
        tvNombre.setText("Nombre: " + actividad.getNombre());
        tvTipo.setText("Tipo: " + (actividad instanceof ActividadAcademica ? "Académica" : "Personal"));
        detPrioridad.setText("Prioridad: " + actividad.getPrioridad());
        detFecha.setText("Fecha Límite: " + actividad.getFechaVencimiento().toLocalDate());

        // 3. Lógica condicional
        if (actividad instanceof ActividadAcademica) {
            tvAsignatura.setVisibility(View.VISIBLE);
            tvAsignatura.setText("Asignatura: " + ((ActividadAcademica) actividad).getAsignatura());
        } else if (actividad instanceof ActividadPersonal) {
            tvLugar.setVisibility(View.VISIBLE);
            tvLugar.setText("Lugar: " + ((ActividadPersonal) actividad).getLugar());
        }

        // 4. Configurar RecyclerView
        rvHistorial.setLayoutManager(new LinearLayoutManager(this));

        actualizarInterfaz(); // Llamamos aquí para llenar avance y tiempo

        findViewById(R.id.btnVolver).setOnClickListener(v -> finish());
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Esto asegura que si vienes del Timer o de Registrar Avance, los números cambien
        actualizarInterfaz();
    }

    private void actualizarInterfaz() {
        if (actividad != null) {
            detAvance.setText("Avance Actual: " + (int)actividad.getAvance() + "%");

            // Usamos el ID detTiempo para mostrar ambos datos (Estimado y Real acumulado)
            detTiempo.setText("Tiempo Estimado Total: " + (int)actividad.getTiempoEstimado() + " min");

            // Actualizar historial
            List<String> sesiones = actividad.getSesionesEnfoque();
            if (sesiones != null) {
                rvHistorial.setAdapter(new HistorialSimpleAdapter(sesiones));
            }
        }
    }

    // Adaptador mejorado para que parezca una tabla
    private class HistorialSimpleAdapter extends RecyclerView.Adapter<HistorialSimpleAdapter.ViewHolder> {
        private List<String> datos;
        public HistorialSimpleAdapter(List<String> datos) { this.datos = datos; }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            // Creamos un LinearLayout horizontal para que los datos queden bajo sus columnas
            LinearLayout layout = new LinearLayout(parent.getContext());
            layout.setOrientation(LinearLayout.HORIZONTAL);
            layout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            layout.setPadding(8, 16, 8, 16);

            for (int i = 0; i < 3; i++) {
                TextView tv = new TextView(parent.getContext());
                tv.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f));
                tv.setGravity(Gravity.CENTER);
                tv.setTextSize(12);
                layout.addView(tv);
            }
            return new ViewHolder(layout);
        }


        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            // Generamos el arreglo dividiendo por la barra vertical
            String[] partes = datos.get(position).split("\\|");

            // IMPORTANTE: Usamos .length para arreglos
            for (int i = 0; i < partes.length && i < 3; i++) {
                View child = holder.layout.getChildAt(i);
                if (child instanceof TextView) {
                    ((TextView) child).setText(partes[i].trim());
                }
            }
        }

        @Override
        public int getItemCount() { return datos.size(); }

        class ViewHolder extends RecyclerView.ViewHolder {
            LinearLayout layout;
            public ViewHolder(View v) { super(v); layout = (LinearLayout) v; }
        }
    }


}
