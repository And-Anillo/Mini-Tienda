package com.minitienda;

import javax.swing.*;
import java.util.*;

public class MiniTienda {

    static ArrayList<String> nombres = new ArrayList<>();
    static double[] precios = new double[0];
    static HashMap<String, Integer> stock = new HashMap<>();
    static double totalCompras = 0;

    public static void main(String[] args) {
        while (true) {
            String opcion = JOptionPane.showInputDialog(null,
                    "1. Agregar producto\n"
                    + "2. Listar inventario\n"
                    + "3. Comprar producto\n"
                    + "4. Mostrar estadísticas\n"
                    + "5. Buscar producto por nombre\n"
                    + "6. Salir con ticket final\n\n"
                    + "Seleccione una opción:");

            if (opcion == null) {
                break;
            }

            switch (opcion) {
                case "1":
                    agregarProducto();
                    break;
                case "2":
                    listarInventario();
                    break;
                case "3":
                    comprarProducto();
                    break;
                case "4":
                    mostrarEstadisticas();
                    break;
                case "5":
                    buscarProducto();
                    break;
                case "6":
                    salir();
                    return;
                default:
                    JOptionPane.showMessageDialog(null, "Opción inválida.");
            }
        }
    }

    static void agregarProducto() {
        String nombre = JOptionPane.showInputDialog("Ingrese el nombre del producto:");
        if (nombre == null || nombre.trim().isEmpty()) {
            mostrarError("Nombre inválido.");
            return;
        }

        if (nombres.contains(nombre)) {
            mostrarError("Producto ya existente.");
            return;
        }

        try {
            String precioStr = JOptionPane.showInputDialog("Ingrese el precio:");
            double precio = Double.parseDouble(precioStr);

            String stockStr = JOptionPane.showInputDialog("Ingrese el stock:");
            int cantStock = Integer.parseInt(stockStr);

            if (precio <= 0 || cantStock < 0) {
                mostrarError("Precio o stock inválido.");
                return;
            }

            nombres.add(nombre);
            precios = expandPrecios(precios, precio);
            stock.put(nombre, cantStock);

            JOptionPane.showMessageDialog(null, "Producto agregado exitosamente.");

        } catch (NumberFormatException e) {
            mostrarError("Entrada numérica inválida.");
        }
    }

    static void listarInventario() {
        if (nombres.isEmpty()) {
            mostrarError("No hay productos registrados.");
            return;
        }

        StringBuilder sb = new StringBuilder("Inventario:\n");
        for (int i = 0; i < nombres.size(); i++) {
            String nombre = nombres.get(i);
            sb.append(String.format("%d. %s - $%.2f - Stock: %d\n", i + 1, nombre, precios[i], stock.get(nombre)));
        }
        JOptionPane.showMessageDialog(null, sb.toString());
    }

    static void comprarProducto() {
        String nombre = JOptionPane.showInputDialog("Ingrese el nombre del producto a comprar:");
        if (nombre == null || nombre.trim().isEmpty()) {
            return;
        }

        int index = indexOfNombre(nombre);
        if (index == -1) {
            mostrarError("Producto no encontrado.");
            return;
        }

        int stockDisponible = stock.get(nombres.get(index));
        if (stockDisponible <= 0) {
            mostrarError("Producto sin stock.");
            return;
        }

        try {
            String cantidadStr = JOptionPane.showInputDialog("Ingrese la cantidad a comprar:");
            int cantidad = Integer.parseInt(cantidadStr);

            if (cantidad <= 0 || cantidad > stockDisponible) {
                mostrarError("Cantidad inválida o insuficiente.");
                return;
            }

            double subtotal = precios[index] * cantidad;
            totalCompras += subtotal;
            stock.put(nombres.get(index), stockDisponible - cantidad);

            JOptionPane.showMessageDialog(null,
                    String.format("Compra realizada.\nTotal: $%.2f", subtotal));

        } catch (NumberFormatException e) {
            mostrarError("Entrada inválida.");
        }
    }

    static void mostrarEstadisticas() {
        if (nombres.isEmpty()) {
            mostrarError("No hay productos.");
            return;
        }

        double min = precios[0], max = precios[0];
        int iMin = 0, iMax = 0;

        for (int i = 1; i < precios.length; i++) {
            if (precios[i] < min) {
                min = precios[i];
                iMin = i;
            }
            if (precios[i] > max) {
                max = precios[i];
                iMax = i;
            }
        }

        String msg = String.format("Producto más barato: %s - $%.2f\nProducto más caro: %s - $%.2f",
                nombres.get(iMin), min, nombres.get(iMax), max);
        JOptionPane.showMessageDialog(null, msg);
    }

    static void buscarProducto() {
        String busqueda = JOptionPane.showInputDialog("Ingrese parte del nombre del producto:");
        if (busqueda == null || busqueda.trim().isEmpty()) {
            return;
        }

        StringBuilder sb = new StringBuilder("Resultados de búsqueda:\n");
        boolean encontrado = false;

        for (int i = 0; i < nombres.size(); i++) {
            if (nombres.get(i).toLowerCase().contains(busqueda.toLowerCase())) {
                sb.append(String.format("%s - $%.2f - Stock: %d\n",
                        nombres.get(i), precios[i], stock.get(nombres.get(i))));
                encontrado = true;
            }
        }

        if (!encontrado) {
            mostrarError("No se encontraron coincidencias.");
        } else {
            JOptionPane.showMessageDialog(null, sb.toString());
        }
    }

    static void salir() {
        JOptionPane.showMessageDialog(null,
                String.format("Gracias por usar el sistema.\nTotal acumulado de compras: $%.2f", totalCompras));
    }

    // Métodos utilitarios
    static int indexOfNombre(String nombre) {
        for (int i = 0; i < nombres.size(); i++) {
            if (nombres.get(i).equalsIgnoreCase(nombre)) {
                return i;
            }
        }
        return -1;
    }

    static double[] expandPrecios(double[] arr, double nuevo) {
        double[] nuevoArr = Arrays.copyOf(arr, arr.length + 1);
        nuevoArr[nuevoArr.length - 1] = nuevo;
        return nuevoArr;
    }

    static void mostrarError(String msg) {
        JOptionPane.showMessageDialog(null, msg, "Error", JOptionPane.ERROR_MESSAGE);

    }
}
