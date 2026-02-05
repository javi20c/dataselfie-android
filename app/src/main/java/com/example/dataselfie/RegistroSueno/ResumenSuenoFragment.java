package com.example.dataselfie.RegistroSueno;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.dataselfie.R;


import java.util.ArrayList;

public class ResumenSuenoFragment extends Fragment {

    private SeguimientoSueno viewModel;
    private RecyclerView recyclerView;
    private DatosSuenoAdapter adapter;
    private TextView textEmptyMessage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sleep_summary, container, false);

        viewModel = new ViewModelProvider(requireActivity()).get(SeguimientoSueno.class);

        // Enlazar vistas
        textEmptyMessage = view.findViewById(R.id.textEmptyMessage);

        // Botón para ir a registrar
        Button btnGoToRegister = view.findViewById(R.id.btnGoToRegister);
        btnGoToRegister.setOnClickListener(v -> {
            RegistroSuenoFragment registerFragment = new RegistroSuenoFragment();
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, registerFragment)
                    .addToBackStack(null)
                    .commit();
        });

        // Configurar RecyclerView
        recyclerView = view.findViewById(R.id.recyclerViewSleep);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new DatosSuenoAdapter(new ArrayList<>());
        recyclerView.setAdapter(adapter);

        // Observar cambios en los registros
        viewModel.getSleepRecords().observe(getViewLifecycleOwner(), records -> {
            if (records == null || records.isEmpty()) {
                textEmptyMessage.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
                textEmptyMessage.setText("No hay registros de sueño esta semana.\nPresiona 'Registrar Horas de Sueño' para empezar.");
            } else {
                textEmptyMessage.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                adapter.updateRecords(viewModel.getUltimos7Dias());
            }
        });

        // Observar mensajes
        viewModel.getMessage().observe(getViewLifecycleOwner(), message -> {
            if (message != null && !message.isEmpty()) {
                Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
            }
        });

        return view;

    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ImageButton btnBack = view.findViewById(R.id.btnBack);

        btnBack.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), com.example.dataselfie.AppDataSelfie.MainActivity.class);
            startActivity(intent);
            requireActivity().finish(); // cierra la activity que contiene el fragment
        });
    }
}