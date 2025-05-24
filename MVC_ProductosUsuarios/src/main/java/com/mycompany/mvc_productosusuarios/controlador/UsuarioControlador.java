package com.mycompany.mvc_productosusuarios.controlador;

import com.mycompany.mvc_productosusuarios.modelo.ConexionDB;
import com.mycompany.mvc_productosusuarios.vista.UsuarioVista;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;

public class UsuarioControlador {
    private final UsuarioVista vista;

    public UsuarioControlador(UsuarioVista vista) {
        this.vista = vista;
        listarUsuarios();

        vista.btnAgregar.addActionListener(e -> agregarUsuario());
        vista.btnEditar.addActionListener(e -> editarUsuario());

        vista.btnEliminar.addActionListener(e -> {
            int fila = vista.tabla.getSelectedRow();
            if (fila >= 0) {
                int confirmacion = JOptionPane.showConfirmDialog(
                    vista,
                    "¿Está seguro de que desea eliminar este usuario?",
                    "Confirmar eliminación",
                    JOptionPane.YES_NO_OPTION
                );
                if (confirmacion == JOptionPane.YES_OPTION) {
                    eliminarUsuario();
                }
            } else {
                JOptionPane.showMessageDialog(vista, "Seleccione un usuario para eliminar.");
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
                vista.txtEmail.setText(vista.tabla.getValueAt(fila, 2).toString());
            }
        });
    }

    private void listarUsuarios() {
        DefaultTableModel modelo = (DefaultTableModel) vista.tabla.getModel();
        modelo.setRowCount(0);
        try (Connection conn = ConexionDB.conectar();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM usuario")) {

            while (rs.next()) {
                modelo.addRow(new Object[]{
                    rs.getInt("id"),
                    rs.getString("nombre"),
                    rs.getString("email")
                });
            }
        } catch (SQLException ex) {
            mostrarError("Error al listar usuarios:\n" + ex.getMessage());
        }
    }

    private void agregarUsuario() {
        try (Connection conn = ConexionDB.conectar();
             PreparedStatement ps = conn.prepareStatement("INSERT INTO usuario (nombre, email) VALUES (?, ?)")) {

            ps.setString(1, vista.txtNombre.getText());
            ps.setString(2, vista.txtEmail.getText());
            ps.executeUpdate();
            listarUsuarios();

        } catch (SQLException ex) {
            mostrarError("Error al agregar usuario:\n" + ex.getMessage());
        }
    }

    private void editarUsuario() {
        int fila = vista.tabla.getSelectedRow();
        if (fila >= 0) {
            int id = (int) vista.tabla.getValueAt(fila, 0);
            try (Connection conn = ConexionDB.conectar();
                 PreparedStatement ps = conn.prepareStatement("UPDATE usuario SET nombre=?, email=? WHERE id=?")) {

                ps.setString(1, vista.txtNombre.getText());
                ps.setString(2, vista.txtEmail.getText());
                ps.setInt(3, id);
                ps.executeUpdate();
                listarUsuarios();

            } catch (SQLException ex) {
                mostrarError("Error al editar usuario:\n" + ex.getMessage());
            }
        } else {
            JOptionPane.showMessageDialog(vista, "Seleccione un usuario para editar.");
        }
    }

    private void eliminarUsuario() {
        int fila = vista.tabla.getSelectedRow();
        if (fila >= 0) {
            int id = (int) vista.tabla.getValueAt(fila, 0);
            try (Connection conn = ConexionDB.conectar();
                 PreparedStatement ps = conn.prepareStatement("DELETE FROM usuario WHERE id=?")) {

                ps.setInt(1, id);
                ps.executeUpdate();
                listarUsuarios();

            } catch (SQLException ex) {
                mostrarError("Error al eliminar usuario:\n" + ex.getMessage());
            }
        }
    }

    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(vista, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
