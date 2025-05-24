package com.mycompany.mvc_productosusuarios;

import com.mycompany.mvc_productosusuarios.vista.*;
import com.mycompany.mvc_productosusuarios.controlador.*;

public class App {
    public static void main(String[] args) {
        String opcion = SeleccionVista.mostrarSeleccion();

        if ("Productos".equals(opcion)) {
            ProductoVista pv = new ProductoVista();
            new ProductoControlador(pv);
            pv.setVisible(true);

        } else if ("Usuarios".equals(opcion)) {
            UsuarioVista uv = new UsuarioVista();
            new UsuarioControlador(uv);
            uv.setVisible(true);
        }
    }
}
