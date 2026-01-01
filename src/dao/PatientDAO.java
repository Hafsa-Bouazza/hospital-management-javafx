package dao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import models.Patient;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static dao.DatabaseConnection.getConnection;

public class PatientDAO {
    private final Connection connection;

    public PatientDAO() throws SQLException {
        this.connection = getConnection();
    }

    // Ajouter un nouveau patient
    public void addPatient(Patient patient) throws SQLException {
        String query = "INSERT INTO patients (nom, prenom, telephone, genre, email) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, patient.getNom());
            stmt.setString(2, patient.getPrenom());
            stmt.setString(3, patient.getTelephone());
            stmt.setString(4, patient.getGenre());
            stmt.setString(5, patient.getEmail());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Échec de l'ajout du patient, aucune ligne affectée.");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    patient.setId(generatedKeys.getInt(1));
                }
            }
        }
    }

    // Mettre à jour un patient existant
    public void updatePatient(Patient patient) throws SQLException {
        String query = "UPDATE patients SET nom = ?, prenom = ?, telephone = ?, genre = ?, email = ? WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, patient.getNom());
            stmt.setString(2, patient.getPrenom());
            stmt.setString(3, patient.getTelephone());
            stmt.setString(4, patient.getGenre());
            stmt.setString(5, patient.getEmail());
            stmt.setInt(6, patient.getId());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Échec de la mise à jour, patient non trouvé.");
            }
        }
    }

    // Supprimer un patient
    public void deletePatient(int id) throws SQLException {
        String query = "DELETE FROM patients WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id);

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Échec de la suppression, patient non trouvé.");
            }
        }
    }

    // Récupérer tous les patients
    public List<Patient> getAllPatients() throws SQLException {
        List<Patient> patients = new ArrayList<>();
        String sql = "SELECT id, nom, prenom, telephone, genre, email FROM patients ORDER BY nom, prenom";

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                patients.add(mapResultSetToPatient(rs));
            }
        }
        return patients;
    }

    // Rechercher des patients par terme
    public List<Patient> searchPatients(String searchTerm) throws SQLException {
        List<Patient> patients = new ArrayList<>();
        String query = "SELECT * FROM patients WHERE nom LIKE ? OR prenom LIKE ? OR telephone LIKE ? ORDER BY nom, prenom";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            String likeTerm = "%" + searchTerm + "%";
            stmt.setString(1, likeTerm);
            stmt.setString(2, likeTerm);
            stmt.setString(3, likeTerm);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    patients.add(mapResultSetToPatient(rs));
                }
            }
        }
        return patients;
    }

    // Récupérer un patient par son ID
    public Patient getPatientById(int id) throws SQLException {
        String query = "SELECT * FROM patients WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToPatient(rs);
                }
            }
        }
        return null;
    }

    // Récupérer la liste des patients d'un médecin (optionnel)
    public List<Patient> getPatientsByMedecin(int medecinId) throws SQLException {
        String sql = "SELECT id, nom, prenom, date_naissance, genre, telephone, email FROM patients WHERE medecin_id = ?";
        List<Patient> patients = new ArrayList<>();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, medecinId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Patient patient = new Patient();
                    patient.setId(rs.getInt("id"));
                    patient.setNom(rs.getString("nom"));
                    patient.setPrenom(rs.getString("prenom"));
                    if (rs.getDate("date_naissance") != null)
                        patient.setDateNaissance(rs.getDate("date_naissance").toLocalDate());
                    patient.setGenre(rs.getString("genre"));
                    patient.setTelephone(rs.getString("telephone"));
                    patient.setEmail(rs.getString("email"));

                    patients.add(patient);
                }
            }
        }
        return patients;
    }

    // Vérifier si un email existe déjà
    public boolean emailExists(String email) throws SQLException {
        String query = "SELECT COUNT(*) FROM patients WHERE email = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, email);

            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        }
    }

    // Vérifier si un email existe pour un autre patient (modification)
    public boolean emailExistsForOtherPatient(String email, int patientId) throws SQLException {
        String query = "SELECT COUNT(*) FROM patients WHERE email = ? AND id != ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, email);
            stmt.setInt(2, patientId);

            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        }
    }

    // Mapper un ResultSet vers un objet Patient
    private Patient mapResultSetToPatient(ResultSet rs) throws SQLException {
        Patient patient = new Patient();
        patient.setId(rs.getInt("id"));
        patient.setNom(rs.getString("nom"));
        patient.setPrenom(rs.getString("prenom"));
        patient.setTelephone(rs.getString("telephone"));
        patient.setGenre(rs.getString("genre"));
        patient.setEmail(rs.getString("email"));

        // Optionnel: gestion date de naissance si colonne présente
        try {
            Date dateNaissance = rs.getDate("date_naissance");
            if (dateNaissance != null) {
                patient.setDateNaissance(dateNaissance.toLocalDate());
            }
        } catch (SQLException ignored) {
            // Pas de colonne date_naissance => ok
        }

        return patient;
    }
}
