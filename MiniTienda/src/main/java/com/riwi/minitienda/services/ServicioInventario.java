/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.riwi.minitienda.services;

import com.riwi.minitienda.model.Producto;

import java.util.List;
/**
 *
 * @author Coder
 */
public interface ServicioInventario {
    void agregarProducto(String nombre, double precio, int stock);
    void actualizarPrecio(int id, double nuevoPrecio);
    void actualizarStock(int id, int nuevoStock);
    void eliminarProducto(int id);
    List<Producto> buscarPorNombre(String nombre);
    List<Producto> obtenerInventario();
}
