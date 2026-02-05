package com.example.dataselfie.AppDataSelfie;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.dataselfie.R;

import java.util.List;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.MenuViewHolder> {

    // Interfaz para manejar clics en las opciones del menú
    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    // Lista de opciones del menú
    private final List<MenuOption> options;

    // Listener para detectar selecciones
    private final OnItemClickListener listener;

    // Constructor del adapter
    public MenuAdapter(List<MenuOption> options, OnItemClickListener listener) {
        this.options = options;
        this.listener = listener;
    }

    @Override
    public MenuViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Infla el layout de cada opción del menú
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_menu_option, parent, false);
        return new MenuViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MenuViewHolder holder, int position) {
        // Asigna datos a la vista según la opción
        MenuOption option = options.get(position);
        holder.tvTitle.setText(option.getTitle());
        holder.ivIcon.setImageResource(option.getIconResId());


        // Maneja el clic en la opción seleccionada
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        // Devuelve el número de opciones del menú
        return options.size();
    }

    static class MenuViewHolder extends RecyclerView.ViewHolder {

        // Referencias a los elementos visuales
        TextView tvTitle;
        ImageView ivIcon;

        public MenuViewHolder(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvOptionTitle);
            ivIcon = itemView.findViewById(R.id.ivIcon);
        }
    }
}

