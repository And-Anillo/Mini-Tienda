/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.riwi.minitienda.config;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
/**
 *
 * @author Coder
 */
public class ConnectionFactory {
    private static final String DB_PROPERTIES = "/db.properties";

    public static Connection getConnection() throws SQLException {
        Properties props = new Properties();
        try (InputStream input = ConnectionFactory.class.getResourceAsStream(DB_PROPERTIES)) {
            if (input == null) {
                throw new RuntimeException("No se encontró el archivo db.properties");
            }
            props.load(input);
        } catch (IOException e) {
            throw new RuntimeException("Error al cargar db.properties", e);
        }

        String url = props.getProperty("db.url");
        String user = props.getProperty("db.user");
        String password = props.getProperty("db.password");
        String driver = props.getProperty("db.driver");

        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Driver JDBC no encontrado", e);
        }

        return DriverManager.getConnection(url, user, password);
    }
}
