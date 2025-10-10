package com.minitienda; // Reemplaza con tu paquete

import com.minitienda.Electrodomestico;
import com.minitienda.Producto;
import javax.swing.JOptionPane;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.text.DecimalFormat;
import java.util.stream.Collectors;

public class MiniTienda {
    // Estructuras de datos
    // Lista de productos (para recorrer y listar)
    private List<Producto> listaProductos; 
    // Mapa para sincronizar stock por nombre (para estadísticas rápidas y validación de existencia)
    private Map<String, Integer> stockPorNombre; 
    // Para formatear la salida de dinero
    private static final DecimalFormat df = new DecimalFormat("0.00");
    // Ticket de compra
    private String ticketFinal = "";
    private double totalCompra = 0.0;

    public MiniTienda() {
        listaProductos = new ArrayList<>();
        stockPorNombre = new HashMap<>();
    }

    public void iniciar() {
        String opcion = "";
        do {
            opcion = mostrarMenu();
            if (opcion == null) { // El usuario presionó Cancelar o cerró la ventana
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
                        break;
                    default:
                        JOptionPane.showMessageDialog(null, "Opción no válida. Inténtelo de nuevo.", "Error de Opción", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Ha ocurrido un error inesperado: " + e.getMessage(), "Error Crítico", JOptionPane.ERROR_MESSAGE);
            }

        } while (!opcion.equals("6"));
    }
    
    // --- Menú principal con JOptionPane ---
    private String mostrarMenu() {
        String menu = "===== MiniTienda con POO y Estructuras de Datos =====\n\n"
                + "1. Agregar producto\n"
                + "2. Listar inventario\n"
                + "3. Comprar producto\n"
                + "4. Estadísticas (Caro/Barato)\n"
                + "5. Buscar producto\n"
                + "6. Salir\n\n"
                + "Seleccione una opción:";
        
        return JOptionPane.showInputDialog(null, menu, "Menú Principal", JOptionPane.PLAIN_MESSAGE);
    }
    
    // --- Tarea 1: Agregar producto ---
    private void agregarProducto() {
        String nombre = null;
        double precio = 0.0;
        int stock = 0;
        
        // Solicita el tipo de producto
        String[] tipos = {"Alimento", "Electrodoméstico"};
        String tipoSeleccionado = (String) JOptionPane.showInputDialog(
                null, 
                "Seleccione el tipo de producto a agregar:",
                "Tipo de Producto",
                JOptionPane.QUESTION_MESSAGE,
                null,
                tipos,
                tipos[0]
        );

        if (tipoSeleccionado == null) return; // Cancelado
        
        // Pide datos genéricos con validación
        try {
            nombre = validarString("Ingrese el nombre del producto (único):");
            if (nombre == null) return;
            
            // Valida que no exista duplicado (ignora mayúsculas/minúsculas)
            if (stockPorNombre.containsKey(nombre.toLowerCase())) {
                JOptionPane.showMessageDialog(null, "Error: El producto ya existe en el inventario.", "Duplicado", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            precio = validarDouble("Ingrese el precio del producto:");
            if (precio == 0.0) return;
            
            stock = validarInt("Ingrese el stock inicial:");
            if (stock == -1) return;

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Error: Ingrese un valor numérico válido para precio o stock.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Producto nuevoProducto = null;
        
        // Pide datos específicos según el tipo
        if (tipoSeleccionado.equals("Alimento")) {
            String fechaCaducidad = JOptionPane.showInputDialog(null, "Ingrese la fecha de caducidad (ej: dd/mm/aaaa):");
            if (fechaCaducidad == null) return;
            nuevoProducto = new Alimentos(nombre, precio, stock, fechaCaducidad);
        } else if (tipoSeleccionado.equals("Electrodoméstico")) {
            try {
                double consumo = validarDouble("Ingrese el consumo energético (kWh):");
                if (consumo == 0.0) return;
                nuevoProducto = new Electrodomestico(nombre, precio, stock, consumo);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Error: Ingrese un valor numérico válido para consumo energético.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
        
        // Añade el producto y actualiza el mapa de stock
        if (nuevoProducto != null) {
            listaProductos.add(nuevoProducto);
            stockPorNombre.put(nombre.toLowerCase(), stock);
            JOptionPane.showMessageDialog(null, "Producto '" + nombre + "' agregado con éxito.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    // --- Tarea 2: Listar inventario ---
    private void listarInventario() {
        if (listaProductos.isEmpty()) {
            JOptionPane.showMessageDialog(null, "El inventario está vacío.", "Inventario", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        StringBuilder sb = new StringBuilder("===== Inventario de Productos (ArrayList y HashMap) =====\n\n");
        int i = 1;
        for (Producto p : listaProductos) {
            // Muestra nombre, precio, stock y descripción (getDescripcion() - Polimorfismo)
            String stockMapa = "Stock Mapa: " + stockPorNombre.getOrDefault(p.getNombre().toLowerCase(), 0);
            sb.append(String.format("%d. %s | %s\n", i++, p.toString(), stockMapa));
        }

        JOptionPane.showMessageDialog(null, sb.toString(), "Listado de Inventario", JOptionPane.PLAIN_MESSAGE);
    }
    
    // --- Tarea 3: Comprar producto ---
    private void comprarProducto() {
        if (listaProductos.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No hay productos para comprar.", "Compra", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            String nombreCompra = validarString("Ingrese el nombre del producto a comprar:");
            if (nombreCompra == null) return;
            
            Producto productoAComprar = buscarProductoPorNombre(nombreCompra);
            
            if (productoAComprar == null) {
                JOptionPane.showMessageDialog(null, "El producto '" + nombreCompra + "' no fue encontrado.", "Error de Compra", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            int stockDisponible = productoAComprar.getStock();
            JOptionPane.showMessageDialog(null, "Stock disponible de " + productoAComprar.getNombre() + ": " + stockDisponible);
            
            if (stockDisponible == 0) {
                 JOptionPane.showMessageDialog(null, "El producto '" + nombreCompra + "' está agotado.", "Error de Stock", JOptionPane.WARNING_MESSAGE);
                 return;
            }
            
            int cantidad = validarInt("Ingrese la cantidad a comprar:");
            if (cantidad == -1) return;
            
            // Valida stock y actualiza
            if (cantidad > stockDisponible) {
                JOptionPane.showMessageDialog(null, "Error: La cantidad solicitada (" + cantidad + ") excede el stock disponible (" + stockDisponible + ").", "Error de Stock", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Genera ticket parcial
            double costoParcial = productoAComprar.getPrecio() * cantidad;
            totalCompra += costoParcial;
            String lineaTicket = String.format("%-25s x%-5d $%-10s\n", 
                                                productoAComprar.getNombre(), 
                                                cantidad, 
                                                df.format(costoParcial));
            
            ticketFinal += lineaTicket;

            // Actualiza stock en ambas estructuras
            productoAComprar.reducirStock(cantidad);
            stockPorNombre.put(productoAComprar.getNombre().toLowerCase(), productoAComprar.getStock());
            
            JOptionPane.showMessageDialog(null, 
                "Compra parcial exitosa!\n" + 
                "Costo parcial: $" + df.format(costoParcial) + 
                "\nNuevo Stock de " + productoAComprar.getNombre() + ": " + productoAComprar.getStock(),
                "Compra Exitosa", JOptionPane.INFORMATION_MESSAGE);

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Error: Ingrese un valor numérico válido para la cantidad.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    // --- Tarea 4: Estadísticas (Caro/Barato) ---
    private void mostrarEstadisticas() {
        if (listaProductos.isEmpty()) {
            JOptionPane.showMessageDialog(null, "El inventario está vacío. No hay estadísticas.", "Estadísticas", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        // Encontrar producto más caro y más barato (con streams para concisión)
        Producto masCaro = listaProductos.stream()
                .max((p1, p2) -> Double.compare(p1.getPrecio(), p2.getPrecio()))
                .orElse(null);

        Producto masBarato = listaProductos.stream()
                .min((p1, p2) -> Double.compare(p1.getPrecio(), p2.getPrecio()))
                .orElse(null);

        String stats = "===== Estadísticas de Precios =====\n\n";
        if (masCaro != null) {
            stats += "Producto más caro:\n" 
                   + "  - " + masCaro.getNombre() + " ($" + df.format(masCaro.getPrecio()) + ")\n"
                   + "  - " + masCaro.getDescripcion() + "\n\n";
        }
        if (masBarato != null) {
            stats += "Producto más barato:\n"
                   + "  - " + masBarato.getNombre() + " ($" + df.format(masBarato.getPrecio()) + ")\n"
                   + "  - " + masBarato.getDescripcion() + "\n";
        }

        JOptionPane.showMessageDialog(null, stats, "Estadísticas", JOptionPane.PLAIN_MESSAGE);
    }
    
    // --- Tarea 5: Buscar producto ---
    private void buscarProducto() {
        try {
            String terminoBusqueda = validarString("Ingrese el nombre o parte del nombre a buscar:");
            if (terminoBusqueda == null) return;
            
            // Permite coincidencias parciales por nombre
            String resultadoBusqueda = listaProductos.stream()
                .filter(p -> p.getNombre().toLowerCase().contains(terminoBusqueda.toLowerCase()))
                .map(p -> p.toString() + " | " + p.getDescripcion())
                .collect(Collectors.joining("\n"));

            if (resultadoBusqueda.isEmpty()) {
                resultadoBusqueda = "No se encontraron productos que coincidan con la búsqueda.";
            } else {
                resultadoBusqueda = "===== Resultados de la búsqueda =====\n\n" + resultadoBusqueda;
            }

            JOptionPane.showMessageDialog(null, resultadoBusqueda, "Búsqueda de Producto", JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception e) {
             JOptionPane.showMessageDialog(null, "Error en la búsqueda: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // --- Tarea 6: Salir ---
    private void salir() {
        // Muestra ticket final
        String mensajeTicket = "===== Ticket Final de Compra =====\n\n"
                             + "Producto                 Cant  Precio\n"
                             + "-----------------------------------\n"
                             + ticketFinal
                             + "-----------------------------------\n"
                             + String.format("%-30s $%-10s\n", "TOTAL FINAL:", df.format(totalCompra))
                             + "\n¡Gracias por su compra!";

        JOptionPane.showMessageDialog(null, mensajeTicket, "Ticket Final", JOptionPane.PLAIN_MESSAGE);
        JOptionPane.showMessageDialog(null, "¡Adiós! El programa ha finalizado.", "Salir", JOptionPane.INFORMATION_MESSAGE);
    }
    
    // --- Búsqueda auxiliar ---
    private Producto buscarProductoPorNombre(String nombre) {
        // Búsqueda exacta e ignora mayúsculas/minúsculas
        return listaProductos.stream()
                .filter(p -> p.getNombre().equalsIgnoreCase(nombre))
                .findFirst()
                .orElse(null);
    }

    // --- VALIDACIONES: Manejo de NumberFormatException y valores no válidos ---
    
    // Valida que el String no sea vacío/nulo y maneja la cancelación
    private String validarString(String mensaje) {
        String input = JOptionPane.showInputDialog(null, mensaje, "Input", JOptionPane.QUESTION_MESSAGE);
        if (input == null) return null; // Cancelado
        if (input.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Error: No se permiten valores vacíos.", "Validación", JOptionPane.ERROR_MESSAGE);
            return validarString(mensaje); // Recursivo
        }
        return input.trim();
    }
    
    // Valida Double, NumberFormatException y valores negativos/cero
    private double validarDouble(String mensaje) {
        String input = JOptionPane.showInputDialog(null, mensaje, "Input", JOptionPane.QUESTION_MESSAGE);
        if (input == null) return 0.0; // Cancelado
        try {
            double valor = Double.parseDouble(input.trim());
            if (valor <= 0) {
                 JOptionPane.showMessageDialog(null, "Error: El valor debe ser positivo.", "Validación", JOptionPane.ERROR_MESSAGE);
                 return validarDouble(mensaje); // Recursivo
            }
            return valor;
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Error: Ingrese un valor numérico válido (ej: 10.50).", "Error de Formato", JOptionPane.ERROR_MESSAGE);
            return validarDouble(mensaje); // Recursivo
        }
    }
    
    // Valida Integer, NumberFormatException y valores negativos/cero
    private int validarInt(String mensaje) {
        String input = JOptionPane.showInputDialog(null, mensaje, "Input", JOptionPane.QUESTION_MESSAGE);
        if (input == null) return -1; // Cancelado
        try {
            int valor = Integer.parseInt(input.trim());
            if (valor < 0) {
                 JOptionPane.showMessageDialog(null, "Error: El valor debe ser cero o positivo.", "Validación", JOptionPane.ERROR_MESSAGE);
                 return validarInt(mensaje); // Recursivo
            }
            return valor;
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Error: Ingrese un número entero válido (ej: 10).", "Error de Formato", JOptionPane.ERROR_MESSAGE);
            return validarInt(mensaje); // Recursivo
        }
    }

    // Método main para ejecutar
    public static void main(String[] args) {
        MiniTienda app = new MiniTienda();
        app.iniciar();
    }
}