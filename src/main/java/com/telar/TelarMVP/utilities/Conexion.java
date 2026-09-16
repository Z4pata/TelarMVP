package com.telar.TelarMVP.utilities;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Conexion {
    private Connection con;

    public Connection obtenerConexion() {
        try {
            //"jdbc:sqlserver://localhost:1433;databaseName=TuBaseDeDatos;user=TuUsuario;password=TuContraseña";
            //Connection connection = DriverManager.getConnection(url)
            //jdbc:oracle:thin:@//localhost:1521/xe
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/prueba", "root", "admin");
        } catch (SQLException ex) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println(ex);
            ex.printStackTrace();
        }
        return con;
    }
}
