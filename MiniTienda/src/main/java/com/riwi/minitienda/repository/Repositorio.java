/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.riwi.minitienda.repository;

import java.util.List;
/**
 *
 * @author Coder
 */
public interface Repositorio<T> {
    void crear(T entidad);
    T buscarPorId(int id);
    List<T> buscarTodos();
    void actualizar(T entidad);
    void eliminar(int id);
}
