package com.mycompany.mvc_productosusuarios.controlador;

import com.mycompany.mvc_productosusuarios.modelo.ConexionDB;
import com.mycompany.mvc_productosusuarios.vista.ProductoVista;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;

public class ProductoControlador {
    private final ProductoVista vista;

    public ProductoControlador(ProductoVista vista) {
        this.vista = vista;
        listarProductos();

        vista.btnAgregar.addActionListener(e -> agregarProducto());
        vista.btnEditar.addActionListener(e -> editarProducto());

        vista.btnEliminar.addActionListener(e -> {
            int fila = vista.tabla.getSelectedRow();
            if (fila >= 0) {
                int confirmacion = JOptionPane.showConfirmDialog(
                    vista,
                    "¿Está seguro de que desea eliminar este producto?",
                    "Confirmar eliminación",
                    JOptionPane.YES_NO_OPTION
                );
                if (confirmacion == JOptionPane.YES_OPTION) {
                    eliminarProducto();
                }
            } else {
                JOptionPane.showMessageDialog(vista, "Seleccione un producto para eliminar.");
            }
        });

        vista.btnSalir.addActionListener(e -> {
            int confirmacion = JOptionPane.showConfirmDialog(
                vista,
                "¿Desea salir de la aplicación?",
                "Salir",
                JOptionPane.YES_NO_OPTION
            );
            if (confirmacion == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });

        vista.tabla.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int fila = vista.tabla.getSelectedRow();
                vista.txtNombre.setText(vista.tabla.getValueAt(fila, 1).toString());
                vista.txtPrecio.setText(vista.tabla.getValueAt(fila, 2).toString());
            }
        });
    }

    private void listarProductos() {
        DefaultTableModel modelo = (DefaultTableModel) vista.tabla.getModel();
        modelo.setRowCount(0);
        try (Connection conn = ConexionDB.conectar();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM producto")) {

            while (rs.next()) {
                modelo.addRow(new Object[]{
                    rs.getInt("id"),
                    rs.getString("nombre"),
                    rs.getDouble("precio")
                });
            }
        } catch (SQLException ex) {
            mostrarError("Error al listar productos:\n" + ex.getMessage());
        }
    }

    private void agregarProducto() {
        String nombre = vista.txtNombre.getText().trim();
        String precioTexto = vista.txtPrecio.getText().trim();

        if (nombre.isEmpty() || precioTexto.isEmpty()) {
            JOptionPane.showMessageDialog(vista, "Todos los campos son obligatorios.");
            return;
        }

        try (Connection conn = ConexionDB.conectar()) {
            // Verificar si ya existe un producto con ese nombre
            PreparedStatement check = conn.prepareStatement("SELECT COUNT(*) FROM producto WHERE nombre = ?");
            check.setString(1, nombre);
            ResultSet rs = check.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                JOptionPane.showMessageDialog(vista, "El producto ya existe. No se permiten duplicados.");
                return;
            }

            // Insertar si no existe
            PreparedStatement ps = conn.prepareStatement("INSERT INTO producto (nombre, precio) VALUES (?, ?)");
            ps.setString(1, nombre);
            ps.setDouble(2, Double.parseDouble(precioTexto));
            ps.executeUpdate();
            listarProductos();

        } catch (SQLException ex) {
            mostrarError("Error al agregar producto:\n" + ex.getMessage());
        } catch (NumberFormatException ex) {
            mostrarError("Precio inválido. Debe ser un número.");
        }
    }

    private void editarProducto() {
        int fila = vista.tabla.getSelectedRow();
        if (fila >= 0) {
            int id = (int) vista.tabla.getValueAt(fila, 0);
            try (Connection conn = ConexionDB.conectar();
                 PreparedStatement ps = conn.prepareStatement("UPDATE producto SET nombre=?, precio=? WHERE id=?")) {

                ps.setString(1, vista.txtNombre.getText());
                ps.setDouble(2, Double.parseDouble(vista.txtPrecio.getText()));
                ps.setInt(3, id);
                ps.executeUpdate();
                listarProductos();

            } catch (SQLException ex) {
                mostrarError("Error al editar producto:\n" + ex.getMessage());
            }
        } else {
            JOptionPane.showMessageDialog(vista, "Seleccione un producto para editar.");
        }
    }

    private void eliminarProducto() {
        int fila = vista.tabla.getSelectedRow();
        if (fila >= 0) {
            int id = (int) vista.tabla.getValueAt(fila, 0);
            try (Connection conn = ConexionDB.conectar();
                 PreparedStatement ps = conn.prepareStatement("DELETE FROM producto WHERE id=?")) {

                ps.setInt(1, id);
                ps.executeUpdate();
                listarProductos();

            } catch (SQLException ex) {
                mostrarError("Error al eliminar producto:\n" + ex.getMessage());
            }
        }
    }

    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(vista, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
