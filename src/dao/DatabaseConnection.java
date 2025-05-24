package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/app1";
    private static final String USER = "root";      
    private static final String PASSWORD = "";     
    
     public static Connection getConnection() {
        try {
            // Charger le driver JDBC (optionnel à partir de JDBC 4.0+)
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Créer et retourner la connexion
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException e) {
            System.out.println("Driver JDBC non trouvé : " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("Erreur de connexion à la base : " + e.getMessage());
        }

        return null;
    }
}

