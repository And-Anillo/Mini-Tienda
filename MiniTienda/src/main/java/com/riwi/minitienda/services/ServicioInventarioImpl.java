/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.riwi.minitienda.services;

import com.riwi.minitienda.model.Producto;
import com.riwi.minitienda.repository.ProductoRepositorioImpl;

import java.util.List;

/**
 *
 * @author Coder
 */
public class ServicioInventarioImpl implements ServicioInventario {

    private final ProductoRepositorioImpl repositorio = new ProductoRepositorioImpl();

    @Override
    public void agregarProducto(String nombre, double precio, int stock) {
        Producto p = new Producto();
        p.setNombre(nombre);
        p.setPrecio(precio);
        p.setStock(stock);
        repositorio.crear(p);
    }

    @Override
    public void actualizarPrecio(int id, double nuevoPrecio) {
        Producto p = repositorio.buscarPorId(id);
        if (p != null) {
            p.setPrecio(nuevoPrecio);
            repositorio.actualizar(p);
        }
    }

    @Override
    public void actualizarStock(int id, int nuevoStock) {
        Producto p = repositorio.buscarPorId(id);
        if (p != null) {
            p.setStock(nuevoStock);
            repositorio.actualizar(p);
        }
    }

    @Override
    public void eliminarProducto(int id) {
        repositorio.eliminar(id);
    }

    @Override
    public List<Producto> buscarPorNombre(String nombre) {
        return repositorio.buscarPorNombre(nombre);
    }

    @Override
    public List<Producto> obtenerInventario() {
        return repositorio.buscarTodos();
    }
}
