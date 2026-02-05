package com.example.dataselfie.RegistroSueno;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.dataselfie.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class DatosSuenoAdapter extends RecyclerView.Adapter<DatosSuenoAdapter.ViewHolder> {

    private List<DatosSueno> records;
    private SimpleDateFormat sdfDia;
    private SimpleDateFormat sdfFecha;

    public DatosSuenoAdapter(List<DatosSueno> records) {
        this.records = records;
        this.sdfDia = new SimpleDateFormat("EEE", Locale.getDefault());
        this.sdfFecha = new SimpleDateFormat("d/M", Locale.getDefault());
    }

    public void updateRecords(List<DatosSueno> newRecords) {
        this.records = newRecords;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_sleep_day, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final DatosSueno record = records.get(position);

        // ====== FORMATEAR FECHA ======
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(record.getDate());

        String diaSemana = formatearDiaSemana(calendar);
        String diaMes = calendar.get(Calendar.DAY_OF_MONTH) + "/" +
                (calendar.get(Calendar.MONTH) + 1);

        holder.textFechaItem.setText(diaSemana + " " + diaMes);
        // ====== FIN FECHA ======

        // ====== CONFIGURAR DATOS ======
        final double horas = record.getHoursSlept();
        final double meta = 8.0;

        holder.textProgreso.setText(String.format(Locale.US, "%.1f/%.1f h", horas, meta));
        holder.textHoras.setText(String.format(Locale.US, "%.1f h", horas));

        // Configurar color según meta
        if (horas >= meta) {
            holder.barraProgreso.setBackgroundColor(
                    holder.itemView.getContext().getResources().getColor(android.R.color.holo_green_dark)
            );
            holder.textProgreso.setTextColor(
                    holder.itemView.getContext().getResources().getColor(android.R.color.holo_green_dark)
            );
        } else {
            holder.barraProgreso.setBackgroundColor(
                    holder.itemView.getContext().getResources().getColor(android.R.color.holo_orange_dark)
            );
            holder.textProgreso.setTextColor(
                    holder.itemView.getContext().getResources().getColor(android.R.color.holo_red_dark)
            );
        }

        // Ajustar ancho de la barra DESPUÉS del layout
        holder.itemView.post(new Runnable() {
            @Override
            public void run() {
                double porcentaje = Math.min((horas / meta) * 100, 100);
                int containerWidth = holder.itemView.getWidth();
                int barWidth = (int) (containerWidth * 0.6 * (porcentaje / 100));

                ViewGroup.LayoutParams params = holder.barraProgreso.getLayoutParams();
                params.width = barWidth;
                holder.barraProgreso.setLayoutParams(params);
            }
        });
    }

    @Override
    public int getItemCount() {
        return records != null ? records.size() : 0;
    }

    //formatear día de la semana
    private String formatearDiaSemana(Calendar calendar) {
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        switch (dayOfWeek) {
            case Calendar.SUNDAY: return "Dom";
            case Calendar.MONDAY: return "Lun";
            case Calendar.TUESDAY: return "Mar";
            case Calendar.WEDNESDAY: return "Mié";
            case Calendar.THURSDAY: return "Jue";
            case Calendar.FRIDAY: return "Vie";
            case Calendar.SATURDAY: return "Sáb";
            default: return "Día";
        }
    }

    // Clase ViewHolder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textFechaItem;
        View barraProgreso;
        TextView textProgreso;
        TextView textHoras;

        public ViewHolder(View itemView) {
            super(itemView);
            textFechaItem = itemView.findViewById(R.id.textFechaItem);
            barraProgreso = itemView.findViewById(R.id.barraProgreso);
            textProgreso = itemView.findViewById(R.id.textProgreso);
            textHoras = itemView.findViewById(R.id.textHoras);
        }
    }
}