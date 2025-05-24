package com.mycompany.mvc_productosusuarios.vista;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class ProductoVista extends JFrame {
    public JTextField txtNombre = new JTextField(15);
    public JTextField txtPrecio = new JTextField(10);
    public JButton btnAgregar = new JButton("Agregar");
    public JButton btnEditar = new JButton("Editar");
    public JButton btnEliminar = new JButton("Eliminar");
    public JButton btnSalir = new JButton("Salir");
    public JTable tabla = new JTable(new DefaultTableModel(new Object[]{"ID", "Nombre", "Precio"}, 0));

    public ProductoVista() {
        setTitle("Gestión de Productos");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new BorderLayout());
        JPanel entrada = new JPanel();

        entrada.add(new JLabel("Nombre:"));
        entrada.add(txtNombre);
        entrada.add(new JLabel("Precio:"));
        entrada.add(txtPrecio);
        entrada.add(btnAgregar);
        entrada.add(btnEditar);
        entrada.add(btnEliminar);
        entrada.add(btnSalir);

        panel.add(entrada, BorderLayout.NORTH);
        panel.add(new JScrollPane(tabla), BorderLayout.CENTER);

        add(panel);

        // Ajusta automáticamente el tamaño de la ventana a los componentes
        pack();
    }
}
