package dao;

import java.sql.*;
import java.util.LinkedHashMap;
import java.util.Map;

public class StatistiquesDAO {

    private static final String URL = "jdbc:mysql://localhost:3306/patient_management";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    // Vérifie que la table existe
    public boolean tableExists() {
        String sql = "SELECT 1 FROM information_schema.tables " +
                "WHERE table_schema = 'patient_management' AND table_name = 'patients' LIMIT 1";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            return rs.next();
        } catch (SQLException e) {
            System.err.println("Erreur de vérification de table: " + e.getMessage());
            return false;
        }
    }

    private Connection getConnection() throws SQLException {
        try {
            // S'assure que le driver est chargé
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException e) {
            throw new SQLException("Driver JDBC introuvable", e);
        }
    }

    public int getTotalPatients() throws SQLException {

        String sql = "SELECT COUNT(*) AS total FROM patients";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            return rs.next() ? rs.getInt("total") : 0;
        }
    }

    public String getMoisPlusCharge() throws SQLException {


        String sql = "SELECT MONTHNAME(created_at) AS mois, COUNT(*) AS total " +
                "FROM patients GROUP BY mois ORDER BY total DESC LIMIT 1";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            return rs.next() ? rs.getString("mois") : "N/A";
        }
    }

    public double getMoyenneMensuelle() throws SQLException {

        String sql = "SELECT COUNT(*) / COUNT(DISTINCT MONTH(created_at)) AS moyenne " +
                "FROM patients";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            return rs.next() ? rs.getDouble("moyenne") : 0.0;
        }
    }

    public Map<String, Integer> getPatientsParMois() throws SQLException {


        Map<String, Integer> data = new LinkedHashMap<>();
        String sql = "SELECT MONTHNAME(created_at) AS mois, COUNT(*) AS total " +
                "FROM patients GROUP BY MONTH(created_at), mois " +
                "ORDER BY MONTH(created_at)";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                data.put(rs.getString("mois"), rs.getInt("total"));
            }
        }
        return data;
    }
}