package dao;

import models.Traitement;
import dao.DatabaseConnection;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TraitementDAO {
    private Connection connection;

    public TraitementDAO() throws SQLException {
        this.connection = DatabaseConnection.getConnection();
    }

    public void addTraitement(Traitement traitement) throws SQLException {
        // Vérifier d'abord si le patient existe
        if (!patientExists(traitement.getNomComplet())) {
            throw new SQLException("Le patient spécifié n'existe pas dans la base de données");
        }

        String query = "INSERT INTO traitements (nom_complet, medecin, medicament, posologie, date_debut, date_fin, instructions, en_cours) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, traitement.getNomComplet());
            stmt.setString(2, traitement.getMedecin());
            stmt.setString(3, traitement.getMedicament());
            stmt.setString(4, traitement.getPosologie());
            stmt.setDate(5, Date.valueOf(traitement.getDateDebut()));
            stmt.setDate(6, traitement.getDateFin() != null ? Date.valueOf(traitement.getDateFin()) : null);
            stmt.setString(7, traitement.getInstructions());
            stmt.setBoolean(8, traitement.isEnCours());

            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Échec de la création du traitement, aucune ligne affectée.");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    traitement.setId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Échec de la création du traitement, aucun ID obtenu.");
                }
            }
        }
    }

    public List<Traitement> getAllTraitements() throws SQLException {
        List<Traitement> traitements = new ArrayList<>();
        String query = "SELECT * FROM traitements ORDER BY date_debut DESC";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Traitement traitement = new Traitement();
                traitement.setId(rs.getInt("id"));
                traitement.setNomComplet(rs.getString("nom_complet"));
                traitement.setMedecin(rs.getString("medecin"));
                traitement.setMedicament(rs.getString("medicament"));
                traitement.setPosologie(rs.getString("posologie"));
                traitement.setDateDebut(rs.getDate("date_debut").toLocalDate());

                Date dateFin = rs.getDate("date_fin");
                traitement.setDateFin(dateFin != null ? dateFin.toLocalDate() : null);

                traitement.setInstructions(rs.getString("instructions"));
                traitement.setEnCours(rs.getBoolean("en_cours"));

                traitements.add(traitement);
            }
        }
        return traitements;
    }

    public void updateTraitement(Traitement traitement) throws SQLException {
        // Vérifier d'abord si le patient existe
        if (!patientExists(traitement.getNomComplet())) {
            throw new SQLException("Le patient spécifié n'existe pas dans la base de données");
        }

        String query = "UPDATE traitements SET nom_complet = ?, medecin = ?, medicament = ?, posologie = ?, " +
                "date_debut = ?, date_fin = ?, instructions = ?, en_cours = ? WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, traitement.getNomComplet());
            stmt.setString(2, traitement.getMedecin());
            stmt.setString(3, traitement.getMedicament());
            stmt.setString(4, traitement.getPosologie());
            stmt.setDate(5, Date.valueOf(traitement.getDateDebut()));
            stmt.setDate(6, traitement.getDateFin() != null ? Date.valueOf(traitement.getDateFin()) : null);
            stmt.setString(7, traitement.getInstructions());
            stmt.setBoolean(8, traitement.isEnCours());
            stmt.setInt(9, traitement.getId());

            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Échec de la mise à jour, traitement non trouvé.");
            }
        }
    }

    public void deleteTraitement(int id) throws SQLException {
        String query = "DELETE FROM traitements WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id);

            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Échec de la suppression, traitement non trouvé.");
            }
        }
    }

    public boolean patientExists(String nomComplet) throws SQLException {
        String query = "SELECT COUNT(*) FROM patients WHERE CONCAT(nom, ' ', prenom) = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, nomComplet);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }

    public Traitement getTraitementById(int id) throws SQLException {
        String query = "SELECT * FROM traitements WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Traitement traitement = new Traitement();
                    traitement.setId(rs.getInt("id"));
                    traitement.setNomComplet(rs.getString("nom_complet"));
                    traitement.setMedecin(rs.getString("medecin"));
                    traitement.setMedicament(rs.getString("medicament"));
                    traitement.setPosologie(rs.getString("posologie"));
                    traitement.setDateDebut(rs.getDate("date_debut").toLocalDate());

                    Date dateFin = rs.getDate("date_fin");
                    traitement.setDateFin(dateFin != null ? dateFin.toLocalDate() : null);

                    traitement.setInstructions(rs.getString("instructions"));
                    traitement.setEnCours(rs.getBoolean("en_cours"));

                    return traitement;
                }
            }
        }
        return null;
    }
}