package com.minitienda;

import javax.swing.JOptionPane;
import java.text.DecimalFormat;

public abstract class Producto {
    // Encapsulamiento: atributos privados
    private String nombre;
    private double precio;
    private int stock;
    
    // Formato para mostrar precios con dos decimales
    private static final DecimalFormat df = new DecimalFormat("0.00");

    // Constructor
    public Producto(String nombre, double precio, int stock) {
        this.nombre = nombre;
        this.precio = precio;
        this.stock = stock;
    }

    // Métodos abstractos (Polimorfismo)
    public abstract String getDescripcion(); 

    // Métodos Getters y Setters (Encapsulamiento)
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }
    
    // Método para reducir el stock después de una compra
    public void reducirStock(int cantidad) {
        this.stock -= cantidad;
    }
    
    // Sobreescritura de toString para una representación clara
    @Override
    public String toString() {
        return "Nombre: " + nombre + 
               " | Precio: $" + df.format(precio) + 
               " | Stock: " + stock +
               " | Tipo: " + this.getClass().getSimpleName();
    }
}
