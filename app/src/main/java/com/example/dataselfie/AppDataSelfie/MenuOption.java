package com.example.dataselfie.AppDataSelfie;

public class MenuOption {

    // Texto que se muestra como título de la opción del menú
    private final String title;

    // Referencia al recurso del ícono asociado a la opción
    private final int iconResId;


    // Constructor que inicializa una opción del menú
    public MenuOption(String title, int iconResId) {
        this.title = title;
        this.iconResId = iconResId;
    }


    // Devuelve el título de la opción
    public String getTitle() {
        return title;
    }

    // Devuelve el id del recurso del ícono
    public int getIconResId() {
        return iconResId;
    }
}

