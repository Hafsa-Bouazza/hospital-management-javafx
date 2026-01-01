package dao;

import dao.DatabaseConnection;
import java.sql.*;

public class ExtendedUserDAO extends UserDAO {
    private Connection connection;

    public ExtendedUserDAO() throws SQLException {
        this.connection = DatabaseConnection.getConnection();
    }

    public String getUsernameByEmail(String email) throws SQLException {
        String query = "SELECT username FROM users WHERE email = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, email);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("username");
                }
            }
        }
        return "Médecin inconnu";
    }
}