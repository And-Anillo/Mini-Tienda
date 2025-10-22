package com.riwi.minitienda;

import com.riwi.minitienda.model.Producto;
import com.riwi.minitienda.services.ServicioInventario;
import com.riwi.minitienda.services.ServicioInventarioImpl;

import javax.swing.*;
import java.util.List;

public class MiniTienda {

    private static final ServicioInventario servicio = new ServicioInventarioImpl();
    private static int operacionesAlta = 0;
    private static int operacionesBaja = 0;
    private static int operacionesActualizacion = 0;

    public static void main(String[] args) {
        String[] opciones = {
            "Agregar producto",
            "Listar inventario",
            "Actualizar precio",
            "Actualizar stock",
            "Eliminar producto",
            "Buscar producto por nombre",
            "Salir"
        };

        while (true) {
            String seleccion = (String) JOptionPane.showInputDialog(
                    null,
                    "Selecciona una opción:",
                    "Mini-Tienda - Menú Principal",
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    opciones,
                    opciones[0]
            );

            if (seleccion == null || seleccion.equals("Salir")) {
                mostrarResumen();
                break;
            }

            switch (seleccion) {
                case "Agregar producto" ->
                    agregarProducto();
                case "Listar inventario" ->
                    listarInventario();
                case "Actualizar precio" ->
                    actualizarPrecio();
                case "Actualizar stock" ->
                    actualizarStock();
                case "Eliminar producto" ->
                    eliminarProducto();
                case "Buscar producto por nombre" ->
                    buscarPorNombre();
            }
        }
    }

    private static void agregarProducto() {
        try {
            String nombre = JOptionPane.showInputDialog("Nombre del producto:");
            if (nombre == null || nombre.trim().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Nombre no válido.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            String precioStr = JOptionPane.showInputDialog("Precio:");
            String stockStr = JOptionPane.showInputDialog("Stock:");

            double precio = Double.parseDouble(precioStr);
            int stock = Integer.parseInt(stockStr);

            if (precio < 0 || stock < 0) {
                JOptionPane.showMessageDialog(null, "Precio y stock deben ser >= 0.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            servicio.agregarProducto(nombre.trim(), precio, stock);
            operacionesAlta++;
            JOptionPane.showMessageDialog(null, "Producto agregado correctamente.");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Formato numérico inválido.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static void listarInventario() {
        List<Producto> productos = servicio.obtenerInventario();
        if (productos.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Inventario vacío.");
            return;
        }
        StringBuilder sb = new StringBuilder("Inventario:\n");
        for (Producto p : productos) {
            sb.append(p.toString()).append("\n");
        }
        JOptionPane.showMessageDialog(null, sb.toString());
    }

    private static void actualizarPrecio() {
        try {
            String idStr = JOptionPane.showInputDialog("ID del producto:");
            int id = Integer.parseInt(idStr);
            String precioStr = JOptionPane.showInputDialog("Nuevo precio:");
            double nuevoPrecio = Double.parseDouble(precioStr);

            if (nuevoPrecio < 0) {
                JOptionPane.showMessageDialog(null, "El precio no puede ser negativo.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            servicio.actualizarPrecio(id, nuevoPrecio);
            operacionesActualizacion++;
            JOptionPane.showMessageDialog(null, "Precio actualizado.");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "ID o precio inválido.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static void actualizarStock() {
        try {
            String idStr = JOptionPane.showInputDialog("ID del producto:");
            int id = Integer.parseInt(idStr);
            String stockStr = JOptionPane.showInputDialog("Nuevo stock:");
            int nuevoStock = Integer.parseInt(stockStr);

            if (nuevoStock < 0) {
                JOptionPane.showMessageDialog(null, "El stock no puede ser negativo.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            servicio.actualizarStock(id, nuevoStock);
            operacionesActualizacion++;
            JOptionPane.showMessageDialog(null, "Stock actualizado.");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "ID o stock inválido.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static void eliminarProducto() {
        try {
            String idStr = JOptionPane.showInputDialog("ID del producto a eliminar:");
            int id = Integer.parseInt(idStr);
            servicio.eliminarProducto(id);
            operacionesBaja++;
            JOptionPane.showMessageDialog(null, "Producto eliminado.");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "ID inválido.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static void buscarPorNombre() {
        String nombre = JOptionPane.showInputDialog("Nombre a buscar (parcial o total):");
        if (nombre == null || nombre.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Búsqueda vacía.");
            return;
        }
        List<Producto> resultados = servicio.buscarPorNombre(nombre.trim());
        if (resultados.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No se encontraron productos.");
        } else {
            StringBuilder sb = new StringBuilder("Resultados:\n");
            for (Producto p : resultados) {
                sb.append(p.toString()).append("\n");
            }
            JOptionPane.showMessageDialog(null, sb.toString());
        }
    }

    private static void mostrarResumen() {
        String resumen = String.format(
                "Resumen de operaciones:\n"
                + "- Altas: %d\n"
                + "- Bajas: %d\n"
                + "- Actualizaciones: %d\n"
                + "¡Gracias por usar la Mini-Tienda!",
                operacionesAlta, operacionesBaja, operacionesActualizacion
        );
        JOptionPane.showMessageDialog(null, resumen, "Resumen Final", JOptionPane.INFORMATION_MESSAGE);
    }
}
