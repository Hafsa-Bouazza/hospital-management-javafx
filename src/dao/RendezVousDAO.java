package dao;

import models.RendezVous;
import models.Patient;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;

import static dao.DatabaseConnection.getConnection;

public class RendezVousDAO {
    private final Connection connection;
    private final PatientDAO patientDAO;

    public RendezVousDAO() throws SQLException {
        this.connection = getConnection();
        this.patientDAO = new PatientDAO();
    }

    // Ajoute un rendez-vous avec medecinId
    public void ajouterRendezVous(RendezVous rendezVous, int medecinId) throws SQLException {
        String query = "INSERT INTO rendez_vous (patient_id, date, heure, medecin_id) VALUES (?, ?, ?, ?)";
        System.out.println("Debug: requête = " + query);
        System.out.println("Debug: patient_id = " + rendezVous.getPatient().getId() +
                ", date = " + rendezVous.getDate() +
                ", heure = " + rendezVous.getHeure() +
                ", medecin_id = " + medecinId);

        try (PreparedStatement stmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, rendezVous.getPatient().getId());
            stmt.setDate(2, Date.valueOf(rendezVous.getDate()));
            stmt.setTime(3, Time.valueOf(rendezVous.getHeure()));
            stmt.setInt(4, medecinId);

            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Échec de la création du rendez-vous, aucune ligne affectée.");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    rendezVous.setId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Échec de la création du rendez-vous, aucun ID obtenu.");
                }
            }
        }
    }

    public ObservableList<RendezVous> getObservableRendezVousList(int medecinId) throws SQLException {
        return listerRendezVous(medecinId);
    }

    public ObservableList<RendezVous> listerRendezVous(int medecinId) throws SQLException {
        ObservableList<RendezVous> rendezVousList = FXCollections.observableArrayList();
        String query = "SELECT r.id, r.patient_id, r.date, r.heure, p.nom, p.prenom " +
                "FROM rendez_vous r JOIN patients p ON r.patient_id = p.id " +
                "WHERE r.medecin_id = ? ORDER BY r.date DESC, r.heure DESC";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, medecinId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Patient patient = new Patient();
                    patient.setId(rs.getInt("patient_id"));
                    patient.setNom(rs.getString("nom"));
                    patient.setPrenom(rs.getString("prenom"));

                    RendezVous rdv = new RendezVous(
                            patient,
                            rs.getDate("date").toLocalDate(),
                            rs.getTime("heure").toLocalTime()
                    );
                    rdv.setId(rs.getInt("id"));
                    rendezVousList.add(rdv);
                }
            }
        }
        return rendezVousList;
    }

    public ObservableList<RendezVous> getAllRendezVous() {
        ObservableList<RendezVous> rendezVousList = FXCollections.observableArrayList();
        String query = "SELECT r.id, r.patient_id, r.date, r.heure, p.nom, p.prenom " +
                "FROM rendez_vous r JOIN patients p ON r.patient_id = p.id " +
                "ORDER BY r.date DESC, r.heure DESC";

        try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Patient patient = new Patient();
                patient.setId(rs.getInt("patient_id"));
                patient.setNom(rs.getString("nom"));
                patient.setPrenom(rs.getString("prenom"));

                RendezVous rdv = new RendezVous(
                        patient,
                        rs.getDate("date").toLocalDate(),
                        rs.getTime("heure").toLocalTime()
                );
                rdv.setId(rs.getInt("id"));
                rendezVousList.add(rdv);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rendezVousList;
    }

    public void supprimerRendezVous(int id) throws SQLException {
        String sql = "DELETE FROM rendez_vous WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            int affected = stmt.executeUpdate();
            if (affected == 0) {
                throw new SQLException("Aucun rendez-vous trouvé avec cet id : " + id);
            }
        }
    }


    public boolean updateRendezVous(RendezVous rdv) throws SQLException {
        String query = "UPDATE rendez_vous SET patient_id = ?, date = ?, heure = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, rdv.getPatient().getId());
            stmt.setDate(2, Date.valueOf(rdv.getDate()));
            stmt.setTime(3, Time.valueOf(rdv.getHeure()));
            stmt.setInt(4, rdv.getId());
            return stmt.executeUpdate() > 0;
        }
    }

    public boolean existeConflitRendezVous(LocalDate date, LocalTime heure, int medecinId) throws SQLException {
        String query = "SELECT COUNT(*) FROM rendez_vous WHERE date = ? AND heure = ? AND medecin_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setDate(1, Date.valueOf(date));
            stmt.setTime(2, Time.valueOf(heure));
            stmt.setInt(3, medecinId);

            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        }
    }
    public void modifierRendezVous(RendezVous rv) throws SQLException {
        String sql = "UPDATE rendez_vous SET patient_id = ?, date = ?, heure = ?, medecin_id = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, rv.getPatient().getId());
            stmt.setDate(2, java.sql.Date.valueOf(rv.getDate()));
            stmt.setTime(3, java.sql.Time.valueOf(rv.getHeure()));
            stmt.setInt(4, rv.getMedecinId());
            stmt.setInt(5, rv.getId());

            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated == 0) {
                throw new SQLException("Aucun rendez-vous trouvé avec l'id " + rv.getId());
            }
        }
    }


    public void close() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la fermeture de la connexion: " + e.getMessage());
        }
    }
}
