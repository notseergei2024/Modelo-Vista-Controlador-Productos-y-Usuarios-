package com.mycompany.mvc_productosusuarios.vista;

import javax.swing.*;

public class SeleccionVista {
    public static String mostrarSeleccion() {
        Object[] opciones = {"Productos", "Usuarios"};
        return (String) JOptionPane.showInputDialog(
            null,
            "Seleccione una opción:",
            "Gestión",
            JOptionPane.PLAIN_MESSAGE,
            null,
            opciones,
            opciones[0]
        );
    }
}
