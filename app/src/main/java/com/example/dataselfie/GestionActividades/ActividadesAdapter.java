package com.example.dataselfie.GestionActividades;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import com.example.dataselfie.R;

public class ActividadesAdapter extends RecyclerView.Adapter<ActividadesAdapter.ViewHolder> {

    private List<GestionActividades> lista;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(int position);
        void onPomodoroClick(int position);
    }

    public ActividadesAdapter(List<GestionActividades> lista, OnItemClickListener listener) {
        this.lista = lista;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_actividad, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        GestionActividades act = lista.get(position);
        android.content.Context contextVista = holder.itemView.getContext();

        // 1. Buscamos el índice REAL en el repositorio para evitar errores al ordenar/filtrar
        int indexReal = RepositorioActividades.getInstance().getLista().indexOf(act);

        // 2. Carga de datos básicos
        holder.tvId.setText("ID: " + (indexReal + 1)); // Corrección del ID dinámico
        holder.tvNombre.setText(act.getNombre());
        holder.tvFecha.setText("Vence: " + act.getFechaVencimiento().toLocalDate().toString());
        holder.tvPrioridad.setText(act.getPrioridad().toString());

        // 3. Lógica de tipo (Académica vs Personal)
        if (act instanceof ActividadAcademica) {
            holder.tvTipo.setText(((ActividadAcademica) act).getTipoAcademico());
        } else {
            holder.tvTipo.setText("PERSONAL");
        }

        // 4. Lógica de visibilidad según avance (100% vs En curso)
        if (act.getAvance() >= 100) {
            holder.btnRegistrarAvance.setVisibility(View.GONE);
            holder.btnPomodoro.setVisibility(View.GONE);
            holder.btnDeepWork.setVisibility(View.GONE);
            holder.tvAvance.setTextColor(android.graphics.Color.parseColor("#4CAF50"));
            holder.tvAvance.setText("¡Completada! 100%");
        } else {
            holder.btnRegistrarAvance.setVisibility(View.VISIBLE);
            holder.tvAvance.setTextColor(android.graphics.Color.BLACK);
            holder.tvAvance.setText("Avance: " + (int)act.getAvance() + "%");

            // Solo mostramos botones de enfoque si es Académica y no está al 100%
            if (act instanceof ActividadAcademica) {
                holder.btnPomodoro.setVisibility(View.VISIBLE);
                holder.btnDeepWork.setVisibility(View.VISIBLE);
            } else {
                holder.btnPomodoro.setVisibility(View.GONE);
                holder.btnDeepWork.setVisibility(View.GONE);
            }
        }

        // --- CLICS DE LOS BOTONES (USANDO INDEXREAL) ---

        holder.btnPomodoro.setOnClickListener(v -> {
            Intent intent = new Intent(contextVista, EnfoqueTimerActivity.class);
            intent.putExtra("EXTRA_POSICION", indexReal);
            intent.putExtra("TIPO_ENFOQUE", "POMODORO");
            intent.putExtra("EXTRA_MINUTOS", 25);
            contextVista.startActivity(intent);
        });

        holder.btnDeepWork.setOnClickListener(v -> {
            Intent intent = new Intent(contextVista, EnfoqueTimerActivity.class);
            intent.putExtra("EXTRA_POSICION", indexReal);
            intent.putExtra("TIPO_ENFOQUE", "DEEP_WORK");
            intent.putExtra("EXTRA_MINUTOS", 45);
            contextVista.startActivity(intent);
        });

        holder.btnDetalles.setOnClickListener(v -> listener.onItemClick(indexReal));

        holder.btnRegistrarAvance.setOnClickListener(v -> {
            Intent intent = new Intent(contextVista, RegistrarAvanceActivity.class);
            intent.putExtra("EXTRA_POSICION", indexReal);
            contextVista.startActivity(intent);
        });

        holder.btnEliminar.setOnClickListener(v -> {
            android.app.Dialog dialog = new android.app.Dialog(contextVista);
            dialog.setContentView(R.layout.dialog_confirmacion);

            if (dialog.getWindow() != null) {
                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            }

            TextView tvMensaje = dialog.findViewById(R.id.tvMensajeDialogo);
            Button btnSi = dialog.findViewById(R.id.btnSiDialogo);
            Button btnNo = dialog.findViewById(R.id.btnNoDialogo);

            tvMensaje.setText("¿Está seguro que desea eliminar la actividad " + act.getNombre() + "?");

            btnSi.setOnClickListener(v1 -> {
                RepositorioActividades.getInstance().getLista().remove(act);
                lista.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, lista.size());
                dialog.dismiss();
                android.widget.Toast.makeText(contextVista, "Actividad eliminada", android.widget.Toast.LENGTH_SHORT).show();
            });

            btnNo.setOnClickListener(v2 -> dialog.dismiss());

            dialog.show();
        });
    }

    @Override
    public int getItemCount() { return lista.size(); }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvId, tvNombre, tvFecha, tvPrioridad, tvAvance, tvTipo;
        Button btnPomodoro, btnDeepWork, btnDetalles, btnRegistrarAvance, btnEliminar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvId = itemView.findViewById(R.id.tvId);
            tvNombre = itemView.findViewById(R.id.tvNombre);
            tvFecha = itemView.findViewById(R.id.tvFecha);
            tvPrioridad = itemView.findViewById(R.id.tvPrioridad);
            tvAvance = itemView.findViewById(R.id.tvAvance);
            tvTipo = itemView.findViewById(R.id.tvTipo);
            btnPomodoro = itemView.findViewById(R.id.btnPomodoro);
            btnDeepWork = itemView.findViewById(R.id.btnDeepWork);
            btnDetalles = itemView.findViewById(R.id.btnDetalles);
            btnRegistrarAvance = itemView.findViewById(R.id.btnRegistrarAvance);
            btnEliminar = itemView.findViewById(R.id.btnEliminar);
        }
    }
}