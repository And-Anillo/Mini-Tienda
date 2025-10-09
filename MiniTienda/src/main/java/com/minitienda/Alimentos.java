package com.minitienda;

public class Alimentos extends Producto {

    private String fechaCaducidad; // Atributo propio

    public Alimentos(String nombre, double precio, int stock, String fechaCaducidad

    
        ) {
        super(nombre, precio, stock);
        this.fechaCaducidad = fechaCaducidad;
    }

    // Implementación polimórfica del método abstracto
    @Override
    public String getDescripcion() {
        return "Alimento - Caducidad: " + fechaCaducidad;
    }

    // Getter y Setter para el atributo propio
    public String getFechaCaducidad() {
        return fechaCaducidad;
    }

    public void setFechaCaducidad(String fechaCaducidad) {
        this.fechaCaducidad = fechaCaducidad;
    }

    // Sobreescritura de toString para incluir la descripción
    @Override
    public String toString() {
        return super.toString() + " | Descripcion: " + getDescripcion();
    }

}
