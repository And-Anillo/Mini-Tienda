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
    
    public static void main(String[] args) {
        
    }
}
