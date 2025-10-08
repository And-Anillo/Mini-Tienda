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
                    "1. Agregar producto\n" +
                    "2. Listar inventario\n" +
                    "3. Comprar producto\n" +
                    "4. Mostrar estadísticas\n" +
                    "5. Buscar producto por nombre\n" +
                    "6. Salir con ticket final\n\n" +
                    "Seleccione una opción:");

            if (opcion == null) break;

            switch (opcion) {
                case "1": agregarProducto(); break;
                case "2": listarInventario(); break;
                case "3": comprarProducto(); break;
                case "4": mostrarEstadisticas(); break;
                case "5": buscarProducto(); break;
                case "6": salir(); return;
                default: JOptionPane.showMessageDialog(null, "Opción inválida.");
            }
        }
    }
}
