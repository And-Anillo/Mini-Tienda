package com.minitienda;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JOptionPane;

public class MiniTienda {

    // Estructuras de datos
    private List<Producto> listaProductos; // ArrayList<Producto>
    private Map<String, Integer> stockPorNombre; // HashMap<String, Integer>

    private double totalCompra = 0.0; // Acumulador para el ticket
    private String ticketParcial = ""; // Para ir guardando el ticket

    public MiniTienda() {
        listaProductos = new ArrayList<>();
        stockPorNombre = new HashMap<>();
    }

    public void iniciar() {
        String opcion = "";
        do {
            opcion = mostrarMenu();
            if (opcion == null) { // Manejar el cierre de la ventana/botón Cancelar
                opcion = "6";
            }

            try {
                switch (opcion) {
                    case "1":
                        agregarProducto();
                        break;
                    case "2":
                        listarInventario();
                        break;
                    case "3":
                        comprarProducto(); // Lo implementaremos a continuación
                        break;
                    case "4":
                        // Estadísticas (se implementa en Tarea 4)
                        break;
                    case "5":
                        // Buscar (se implementa en Tarea 4)
                        break;
                    case "6":
                        salir();
                        break;
                    default:
                        JOptionPane.showMessageDialog(null, "Opción no válida.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Error inesperado: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        } while (!opcion.equals("6"));
    }

    private String mostrarMenu() {
        String menu = "===== MiniTienda ====\n"
                + "1. Agregar producto\n"
                + "2. Listar inventario\n"
                + "3. Comprar producto\n"
                + "4. Estadísticas\n"
                + "5. Buscar producto\n"
                + "6. Salir\n\n"
                + "Seleccione una opción:";

        return JOptionPane.showInputDialog(null, menu, "Menú Tienda", JOptionPane.PLAIN_MESSAGE);
    }

    // Implementación de Agregar producto (TAREA 3.1)
    private void agregarProducto() {
        // 1. Pide tipo (Alimento/Electrodoméstico)
        String[] tipos = {"Alimento", "Electrodoméstico"};
        String tipo = (String) JOptionPane.showInputDialog(null, "Seleccione tipo:", "Agregar Producto",
                JOptionPane.QUESTION_MESSAGE, null, tipos, tipos[0]);
        if (tipo == null) {
            return;
        }

        // 2. Solicita datos (nombre, precio, stock)
        try {
            String nombre = validarString("Ingrese nombre:");
            if (nombre == null) {
                return;
            }

            // 3. Valida que no exista duplicado (usando HashMap)
            if (stockPorNombre.containsKey(nombre.toLowerCase())) {
                JOptionPane.showMessageDialog(null, "Error: Producto ya existe.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            double precio = validarDouble("Ingrese precio:");
            if (precio == 0.0) {
                return;
            }

            int stock = validarInt("Ingrese stock:");
            if (stock == -1) {
                return;
            }

            Producto p = null;
            if (tipo.equals("Alimento")) {
                String caducidad = validarString("Ingrese fecha de caducidad:");
                if (caducidad == null) {
                    return;
                }
                p = new Alimentos(nombre, precio, stock, caducidad);
            } else if (tipo.equals("Electrodoméstico")) {
                double consumo = validarDouble("Ingrese consumo energético:");
                if (consumo == 0.0) {
                    return;
                }
                p = new Electrodomestico(nombre, precio, stock, consumo);
            }

            // Añadir a ambas estructuras
            if (p != null) {
                listaProductos.add(p);
                stockPorNombre.put(nombre.toLowerCase(), stock); // Almacenar con nombre en minúsculas
                JOptionPane.showMessageDialog(null, "Producto agregado con éxito.");
            }
        } catch (NumberFormatException e) {
            // Esto lo manejamos principalmente en los métodos validarDouble/validarInt
        }
    }

    // Implementación de Listar inventario (TAREA 3.2)
    private void listarInventario() {
        if (listaProductos.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Inventario vacío.");
            return;
        }

        StringBuilder sb = new StringBuilder("===== Inventario Actual =====\n");
        // Recorre ArrayList y usa getDescripcion() (Polimorfismo)
        for (int i = 0; i < listaProductos.size(); i++) {
            Producto p = listaProductos.get(i);
            sb.append(String.format("%d. %s\n", (i + 1), p.toString()));
        }
        JOptionPane.showMessageDialog(null, sb.toString());
    }

// Implementación de Comprar producto (TAREA 3.3)
    private void comprarProducto() {
        if (listaProductos.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No hay productos para comprar.");
            return;
        }

        String nombreCompra = validarString("Ingrese el nombre del producto a comprar:");
        if (nombreCompra == null) {
            return;
        }

        Producto productoAComprar = buscarProductoPorNombre(nombreCompra);

        if (productoAComprar == null) {
            JOptionPane.showMessageDialog(null, "Producto no encontrado.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int cantidad = validarInt("Stock disponible: " + productoAComprar.getStock() + "\nIngrese cantidad a comprar:");
        if (cantidad == -1) {
            return;
        }

        // Valida stock y cantidad
        if (cantidad > productoAComprar.getStock()) {
            JOptionPane.showMessageDialog(null, "Stock insuficiente.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Genera ticket parcial
        double costoParcial = productoAComprar.getPrecio() * cantidad;
        totalCompra += costoParcial;

        String linea = String.format("%-20s x %-5d = $%.2f\n",
                productoAComprar.getNombre(), cantidad, costoParcial);
        ticketParcial += linea;

        // Actualiza stock en ambas estructuras
        productoAComprar.reducirStock(cantidad);
        stockPorNombre.put(nombreCompra.toLowerCase(), productoAComprar.getStock()); // Actualiza HashMap

        JOptionPane.showMessageDialog(null, "Compra parcial: $" + String.format("%.2f", costoParcial) + "\nStock restante: " + productoAComprar.getStock());
    }

    private Producto buscarProductoPorNombre(String nombre) {
        // Busca en la lista de productos (ArrayList)
        for (Producto p : listaProductos) {
            if (p.getNombre().equalsIgnoreCase(nombre)) {
                return p;
            }
        }
        return null;
    }

    private void salir() {
        // Muestra ticket final con total de compras
        String ticketFinal = "===== Ticket Final =====\n"
                + "Producto             Cant  Total\n"
                + "----------------------------------\n"
                + this.ticketParcial
                + "----------------------------------\n"
                + String.format("TOTAL: %31s%.2f", "$", this.totalCompra);

        JOptionPane.showMessageDialog(null, ticketFinal, "Ticket Final", JOptionPane.INFORMATION_MESSAGE);
    }

    // Implementación de Estadísticas (TAREA 4.1)
    private void mostrarEstadisticas() {
        if (listaProductos.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No hay productos para estadísticas.");
            return;
        }

        Producto masCaro = null;
        Producto masBarato = null;

        // Inicializar con el primer producto
        if (!listaProductos.isEmpty()) {
            masCaro = listaProductos.get(0);
            masBarato = listaProductos.get(0);
        }

        // Recorre y compara
        for (Producto p : listaProductos) {
            if (p.getPrecio() > masCaro.getPrecio()) {
                masCaro = p;
            }
            if (p.getPrecio() < masBarato.getPrecio()) {
                masBarato = p;
            }
        }

        String stats = "===== Estadísticas =====\n\n";
        stats += "Producto más caro:\n" + masCaro.toString() + "\n\n";
        stats += "Producto más barato:\n" + masBarato.toString();

        JOptionPane.showMessageDialog(null, stats, "Estadísticas", JOptionPane.INFORMATION_MESSAGE);
    }

    // Implementación de Buscar producto (TAREA 4.2)
    private void buscarProducto() {
        String busqueda = validarString("Ingrese nombre (o parte) para buscar:");
        if (busqueda == null) {
            return;
        }

        StringBuilder resultados = new StringBuilder("===== Resultados de Búsqueda =====\n");
        boolean encontrado = false;

        // Permite coincidencias parciales por nombre
        for (Producto p : listaProductos) {
            if (p.getNombre().toLowerCase().contains(busqueda.toLowerCase())) {
                resultados.append(p.toString()).append("\n");
                encontrado = true;
            }
        }

        if (!encontrado) {
            JOptionPane.showMessageDialog(null, "No se encontraron coincidencias.", "Búsqueda", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, resultados.toString(), "Resultados", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    // --- VALIDACIONES (TAREA 4.3 y 4.4) ---
    // Valida que el String no sea vacío/nulo
    private String validarString(String mensaje) {
        String input = JOptionPane.showInputDialog(null, mensaje);
        if (input == null) {
            return null; // Cancelado
        }
        if (input.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "No se permiten valores vacíos.", "Validación", JOptionPane.ERROR_MESSAGE);
            return validarString(mensaje); // Pide de nuevo
        }
        return input.trim();
    }

    // Valida double, NumberFormatException, y que sea > 0
    private double validarDouble(String mensaje) {
        String input = JOptionPane.showInputDialog(null, mensaje);
        if (input == null) {
            return 0.0; // Cancelado
        }
        try {
            double valor = Double.parseDouble(input.trim());
            if (valor <= 0) {
                JOptionPane.showMessageDialog(null, "El valor debe ser positivo (> 0).", "Validación", JOptionPane.ERROR_MESSAGE);
                return validarDouble(mensaje); // Pide de nuevo
            }
            return valor;
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Error: Ingrese un valor numérico válido.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
            return validarDouble(mensaje); // Pide de nuevo
        }
    }

    // Valida int, NumberFormatException, y que sea >= 0
    private int validarInt(String mensaje) {
        String input = JOptionPane.showInputDialog(null, mensaje);
        if (input == null) {
            return -1; // Cancelado
        }
        try {
            int valor = Integer.parseInt(input.trim());
            if (valor < 0) {
                JOptionPane.showMessageDialog(null, "El valor no puede ser negativo.", "Validación", JOptionPane.ERROR_MESSAGE);
                return validarInt(mensaje); // Pide de nuevo
            }
            return valor;
        } catch (NumberFormatException e) {
            // Manejar NumberFormatException
            JOptionPane.showMessageDialog(null, "Error: Ingrese un valor entero válido.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
            return validarInt(mensaje); // Pide de nuevo
        }
    }

    public static void main(String[] args) {
        MiniTienda app = new MiniTienda();
        app.iniciar();
    }
}
