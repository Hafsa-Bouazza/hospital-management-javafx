package controllers;

import dao.PatientDAO;
import dao.RendezVousDAO;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import models.RendezVous;
import models.Patient;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class RendezVousController implements MedecinIdHolder {

    @FXML private TextField idPatientField;
    @FXML private DatePicker datePicker;
    @FXML private TextField timeField;
    @FXML private Button validerButton, annulerButton;
    @FXML private TableColumn<RendezVous, Void> colActions;
    @FXML private TableView<RendezVous> tableRendezVous;
    @FXML private TableColumn<RendezVous, String> colPatient, colDate, colHeure;
    @FXML private Button btnAccueil, btnPatients, btnTraitements, btnStats, btnRdv, btnParametres;

    private static final ObservableList<RendezVous> rendezVousList = FXCollections.observableArrayList();
    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private PatientDAO patientDAO;
    private RendezVousDAO rendezVousDAO;
    private RendezVous rendezVousSelectionne;
    private int medecinId;

    @FXML
    public void initialize() {
        try {
            patientDAO = new PatientDAO();
            rendezVousDAO = new RendezVousDAO();
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Erreur DB", "Impossible de se connecter à la base de données.");
        }

        colPatient.setCellValueFactory(cellData -> {
            Patient p = cellData.getValue().getPatient();
            return new ReadOnlyStringWrapper(p != null ? p.getNom() + " " + p.getPrenom() : "");
        });

        colDate.setCellValueFactory(cellData -> {
            if (cellData.getValue().getDate() != null)
                return new ReadOnlyStringWrapper(cellData.getValue().getDate().format(dateFormatter));
            else
                return new ReadOnlyStringWrapper("");
        });

        colHeure.setCellValueFactory(cellData -> {
            LocalTime lt = cellData.getValue().getHeure();
            return new ReadOnlyStringWrapper(lt != null ? lt.format(timeFormatter) : "");
        });

        addActionsToTable();

        tableRendezVous.setItems(rendezVousList);
        tableRendezVous.setEditable(false);
        datePicker.setValue(LocalDate.now());
    }

    private void addActionsToTable() {
        colActions.setCellFactory(param -> new TableCell<>() {
            private final Button btnModifier = new Button("Modifier");
            private final Button btnSupprimer = new Button("Supprimer");
            private final HBox pane = new HBox(10, btnModifier, btnSupprimer);

            {
                btnModifier.setStyle("-fx-background-color: #fdd835; -fx-text-fill: black;");
                btnSupprimer.setStyle("-fx-background-color: #8735e5; -fx-text-fill: white;");

                btnModifier.setOnAction(event -> {
                    RendezVous rv = getTableView().getItems().get(getIndex());
                    rendezVousSelectionne = rv;
                    idPatientField.setText(String.valueOf(rv.getPatient().getId()));
                    datePicker.setValue(rv.getDate());
                    timeField.setText(rv.getHeure().format(timeFormatter));
                });

                btnSupprimer.setOnAction(event -> {
                    RendezVous rv = getTableView().getItems().get(getIndex());
                    try {
                        rendezVousDAO.supprimerRendezVous(rv.getId());
                        rendezVousList.remove(rv);
                        tableRendezVous.refresh();
                    } catch (SQLException e) {
                        showAlert("Erreur", "Suppression échouée : " + e.getMessage());
                        e.printStackTrace();
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : pane);
            }
        });
    }

    @Override
    public void setMedecinId(int medecinId) {
        this.medecinId = medecinId;
        initializeData();
    }

    private void initializeData() {
        try {
            ObservableList<RendezVous> freshRdvs = rendezVousDAO.getObservableRendezVousList(medecinId);
            Platform.runLater(() -> {
                rendezVousList.clear();
                rendezVousList.addAll(freshRdvs);
                tableRendezVous.refresh();
            });
        } catch (SQLException e) {
            showAlert("Erreur", "Impossible de charger les données: " + e.getMessage());
        }
    }

    @FXML
    private void handleValider(ActionEvent event) {
        try {
            int idPatient = Integer.parseInt(idPatientField.getText().trim());
            Patient patient = patientDAO.getPatientById(idPatient);
            if (patient == null) {
                showAlert("Erreur", "Patient introuvable !");
                return;
            }

            LocalDate date = datePicker.getValue();
            if (date == null) {
                showAlert("Erreur", "Veuillez sélectionner une date.");
                return;
            }

            String heureStr = timeField.getText().trim();
            if (heureStr.isEmpty()) {
                showAlert("Erreur", "Veuillez saisir une heure.");
                return;
            }

            LocalTime heure = LocalTime.parse(heureStr, timeFormatter);

            if (rendezVousSelectionne != null) {
                rendezVousSelectionne.setPatient(patient);
                rendezVousSelectionne.setDate(date);
                rendezVousSelectionne.setHeure(heure);
                rendezVousSelectionne.setMedecinId(medecinId);

                rendezVousDAO.modifierRendezVous(rendezVousSelectionne);
                rendezVousSelectionne = null;
            } else {
                RendezVous rv = new RendezVous();
                rv.setPatient(patient);
                rv.setDate(date);
                rv.setHeure(heure);
                rv.setMedecinId(medecinId);

                rendezVousDAO.ajouterRendezVous(rv, medecinId);
            }

            initializeData();
            resetForm();

        } catch (NumberFormatException e) {
            showAlert("Erreur", "ID du patient invalide.");
        } catch (DateTimeParseException e) {
            showAlert("Erreur", "Format d'heure invalide. Utilisez HH:mm (ex: 14:30).");
        } catch (SQLException e) {
            showAlert("Erreur BD", "Impossible d'enregistrer le rendez-vous : " + e.getMessage());
        }
    }

    @FXML
    private void handleAnnuler(ActionEvent event) {
        resetForm();
    }

    private void resetForm() {
        idPatientField.clear();
        datePicker.setValue(LocalDate.now());
        timeField.clear();
        rendezVousSelectionne = null;
    }

    // Navigation
    @FXML private void handleAccueil() throws IOException { navigateTo("/views/HomePage.fxml", btnAccueil, "Home Page"); }
    @FXML private void handlePatients() throws IOException { navigateTo("/views/PatientInterface.fxml", btnPatients, "Patients"); }
    @FXML private void handleTraitements() throws IOException { navigateTo("/views/Traitement.fxml", btnTraitements, "Traitements"); }
    @FXML private void handleStats() throws IOException { navigateTo("/views/statistiques.fxml", btnStats, "Statistiques"); }
    @FXML private void handleRdv() throws IOException { navigateTo("/views/rdv.fxml", btnRdv, "Rendez Vous"); }
    @FXML private void handleParametres() throws IOException { navigateTo("/views/Parametres.fxml", btnParametres, "Parametres"); }

    private void navigateTo(String fxmlPath, Button sourceButton, String title) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
        Parent root = loader.load();
        Object controller = loader.getController();
        if (controller instanceof MedecinIdHolder) {
            ((MedecinIdHolder) controller).setMedecinId(medecinId);
        }
        Stage stage = (Stage) sourceButton.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle(title);
        stage.show();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
