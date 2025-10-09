package com.minitienda;

public class Electrodomestico extends Producto {
    private double consumoEnergetico; // Atributo propio

    public Electrodomestico(String nombre, double precio, int stock, double consumoEnergetico) {
        super(nombre, precio, stock);
        this.consumoEnergetico = consumoEnergetico;
    }

    // Implementación polimórfica del método abstracto
    @Override
    public String getDescripcion() {
        return "Electrodoméstico - Consumo: " + consumoEnergetico + " kWh";
    }

    // Getter y Setter para el atributo propio
    public double getConsumoEnergetico() {
        return consumoEnergetico;
    }

    public void setConsumoEnergetico(double consumoEnergetico) {
        this.consumoEnergetico = consumoEnergetico;
    }
    
    // Sobreescritura de toString para incluir la descripción
    @Override
    public String toString() {
        return super.toString() + " | Descripcion: " + getDescripcion();
    }
}
