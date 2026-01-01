package controllers;

import dao.PatientDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import models.Patient;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;
import javafx.fxml.Initializable;
import java.net.URL;
import java.util.ResourceBundle;


public class PatientController implements Initializable {

    @FXML private TextField searchField;
    @FXML private TableView<Patient> patientTable;
    @FXML private TableColumn<Patient, Integer> idColumn;
    @FXML private TableColumn<Patient, String> nomColumn;
    @FXML private TableColumn<Patient, String> prenomColumn;
    @FXML private TableColumn<Patient, String> telephoneColumn;
    @FXML private TableColumn<Patient, String> genreColumn;
    @FXML private TableColumn<Patient, String> emailColumn;
    @FXML private TableColumn<Patient, Void> actionsColumn;

    @FXML private TextField nomField;
    @FXML private TextField prenomField;
    @FXML private TextField telephoneField;
    @FXML private ComboBox<String> genreCombo;
    @FXML private TextField emailField;
    @FXML private Button saveButton;

    private PatientDAO patientDAO;
    private ObservableList<Patient> patientsList;
    private Patient patientToEdit = null;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            patientDAO = new PatientDAO(); 
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        setupGenreCombo();
        setupTable();
        loadPatients(); 
    }

    private void setupTable() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nomColumn.setCellValueFactory(new PropertyValueFactory<>("nom"));
        prenomColumn.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        telephoneColumn.setCellValueFactory(new PropertyValueFactory<>("telephone"));
        genreColumn.setCellValueFactory(new PropertyValueFactory<>("genre"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));

        // Ajout des boutons d'action
        actionsColumn.setCellFactory(param -> new TableCell<>() {
            private final Button editButton = new Button("Modifier");
            private final Button deleteButton = new Button("Supprimer");

            {
                // Style des boutons
                editButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
                deleteButton.setStyle("-fx-background-color: #f44336; -fx-text-fill: white;");

                // Actions des boutons
                editButton.setOnAction(event -> {
                    Patient patient = getTableView().getItems().get(getIndex());
                    editPatient(patient);
                });

                deleteButton.setOnAction(event -> {
                    Patient patient = getTableView().getItems().get(getIndex());
                    deletePatient(patient);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    HBox buttons = new HBox(5, editButton, deleteButton);
                    setGraphic(buttons);
                }
            }
        });
    }



    @FXML
    private void setupGenreCombo() {
        genreCombo.setItems(FXCollections.observableArrayList(
                "Homme", "Femme", "Autre"
        ));
    }

    private void loadPatients() {
        try {
            patientsList = FXCollections.observableArrayList(patientDAO.getAllPatients());
            patientTable.setItems(patientsList);
        } catch (SQLException e) {
            showAlert("Erreur", "Impossible de charger les patients", Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    @FXML
    private void handleSearch(ActionEvent event) {
        String searchTerm = searchField.getText().trim();
        if (!searchTerm.isEmpty()) {
            try {
                patientsList = FXCollections.observableArrayList(patientDAO.searchPatients(searchTerm));
                patientTable.setItems(patientsList);
            } catch (SQLException e) {
                showAlert("Erreur", "Erreur lors de la recherche", Alert.AlertType.ERROR);
                e.printStackTrace();
            }
        } else {
            loadPatients();
        }
    }

    @FXML
    private void handleSavePatient(ActionEvent event) {
        // Validation des champs
        if (nomField.getText().isEmpty() || prenomField.getText().isEmpty() ||
                telephoneField.getText().isEmpty() || genreCombo.getValue() == null) {
            showAlert("Validation", "Veuillez remplir tous les champs obligatoires", Alert.AlertType.WARNING);
            return;
        }

        try {
            Patient patient = new Patient();
            patient.setNom(nomField.getText());
            patient.setPrenom(prenomField.getText());
            patient.setTelephone(telephoneField.getText());
            patient.setGenre(genreCombo.getValue());
            patient.setEmail(emailField.getText());

            if (patientToEdit == null) {
                // Ajout d'un nouveau patient
                patientDAO.addPatient(patient);
                showAlert("Succès", "Patient ajouté avec succès", Alert.AlertType.INFORMATION);
            } else {
                // Modification d'un patient existant
                patient.setId(patientToEdit.getId());
                patientDAO.updatePatient(patient);
                showAlert("Succès", "Patient modifié avec succès", Alert.AlertType.INFORMATION);
                patientToEdit = null;
                saveButton.setText("Enregistrer");
            }

            clearForm();
            loadPatients();
        } catch (SQLException e) {
            showAlert("Erreur", "Erreur lors de l'enregistrement", Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    private void editPatient(Patient patient) {
        patientToEdit = patient;
        nomField.setText(patient.getNom());
        prenomField.setText(patient.getPrenom());
        telephoneField.setText(patient.getTelephone());
        genreCombo.setValue(patient.getGenre());
        emailField.setText(patient.getEmail());
        saveButton.setText("Modifier");
    }

    private void deletePatient(Patient patient) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation de suppression");
        alert.setHeaderText("Supprimer le patient");
        alert.setContentText("Êtes-vous sûr de vouloir supprimer ce patient ?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                patientDAO.deletePatient(patient.getId());
                loadPatients();
                showAlert("Succès", "Patient supprimé avec succès", Alert.AlertType.INFORMATION);
            } catch (SQLException e) {
                showAlert("Erreur", "Erreur lors de la suppression", Alert.AlertType.ERROR);
                e.printStackTrace();
            }
        }
    }

    private void clearForm() {
        nomField.clear();
        prenomField.clear();
        telephoneField.clear();
        genreCombo.getSelectionModel().clearSelection();
        emailField.clear();
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Méthodes de navigation
    @FXML
    private void goAccueil(ActionEvent event) throws IOException {
        loadView("/views/HomePage.fxml","Home Page");
    }

    @FXML
    private void goPatients(ActionEvent event) throws IOException {
        loadView("/views/PatientInterface.fxml","Patiets");
    }

    @FXML
    private void goTraitements(ActionEvent event) throws IOException {
        loadView("/views/Traitement.fxml","Traitements");
    }

    @FXML
    private void goStats(ActionEvent event) throws IOException {
        loadView("/views/statistiques.fxml","Statistiques");
    }

    @FXML
    private void goRendezVous(ActionEvent event) throws IOException {
        loadView("/views/rdv.fxml","RendezVous");
    }

    @FXML
    private void goParametres(ActionEvent event) throws IOException {
        loadView("/views/Parametres.fxml","Paramètres");
    }

    private void loadView(String fxmlPath, String title) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
        Stage stage = (Stage) patientTable.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle(title);
        stage.show();
    }
}