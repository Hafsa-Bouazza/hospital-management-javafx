package controllers;

import dao.TraitementDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.TextArea;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import models.Traitement;

import java.awt.*;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Optional;

public class TraitementController {

    @FXML private TabPane mainTabPane;
    @FXML private Tab listTab;
    @FXML private Tab formTab;

    @FXML private TableView<Traitement> traitementTable;
    @FXML private TableColumn<Traitement, Integer> idCol;
    @FXML private TableColumn<Traitement, String> patientCol;
    @FXML private TableColumn<Traitement, String> medecinCol;
    @FXML private TableColumn<Traitement, String> medicamentCol;
    @FXML private TableColumn<Traitement, String> posologieCol;
    @FXML private TableColumn<Traitement, LocalDate> dateDebutCol;
    @FXML private TableColumn<Traitement, LocalDate> dateFinCol;
    @FXML private TableColumn<Traitement, String> statutCol;
    @FXML private TableColumn<Traitement, Void> optionsCol;

    @FXML private TextField idField;
    @FXML private TextField nomCompletField;
    @FXML private TextField medecinField;
    @FXML private TextField medicamentField;
    @FXML private TextField posologieField;
    @FXML private DatePicker dateDebutPicker;
    @FXML private DatePicker dateFinPicker;
    @FXML private TextArea instructionsArea;
    @FXML private CheckBox enCoursCheck;

    @FXML private Button btnNouveauTraitement;
    @FXML private Button btnEnregistrer;
    @FXML private Button btnAnnuler;

    private TraitementDAO traitementDAO;
    private ObservableList<Traitement> traitementsList;
    private Traitement traitementEnCours = null;
    @FXML
    public void initialize() {
        try {
            traitementDAO = new TraitementDAO();
            setupTable();
            loadTraitements();
            formTab.setDisable(true);
        } catch (SQLException e) {
            showAlert("Erreur", "Erreur de connexion à la base de données", Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    private void setupTable() {
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        patientCol.setCellValueFactory(new PropertyValueFactory<>("nomComplet"));
        medecinCol.setCellValueFactory(new PropertyValueFactory<>("medecin"));
        medicamentCol.setCellValueFactory(new PropertyValueFactory<>("medicament"));
        posologieCol.setCellValueFactory(new PropertyValueFactory<>("posologie"));
        dateDebutCol.setCellValueFactory(new PropertyValueFactory<>("dateDebut"));
        dateFinCol.setCellValueFactory(new PropertyValueFactory<>("dateFin"));
        statutCol.setCellValueFactory(new PropertyValueFactory<>("statut"));

        // Ajout des boutons Modifier et Supprimer
        optionsCol.setCellFactory(param -> new TableCell<>() {
            private final Button editButton = new Button("Modifier");
            private final Button deleteButton = new Button("Supprimer");

            {
                // Style des boutons
                editButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
                deleteButton.setStyle("-fx-background-color: #f44336; -fx-text-fill: white;");

                // Actions des boutons
                editButton.setOnAction(event -> {
                    Traitement traitement = getTableView().getItems().get(getIndex());
                    handleEditTraitement(traitement);
                });

                deleteButton.setOnAction(event -> {
                    Traitement traitement = getTableView().getItems().get(getIndex());
                    handleDeleteTraitement(traitement);
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

    private void loadTraitements() {
        try {
            traitementsList = FXCollections.observableArrayList(traitementDAO.getAllTraitements());
            traitementTable.setItems(traitementsList);
        } catch (SQLException e) {
            showAlert("Erreur", "Impossible de charger les traitements", Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    @FXML
    private void handleNouveauTraitement() {
        clearForm();
        mainTabPane.getSelectionModel().select(formTab);
        formTab.setDisable(false);
    }

    @FXML
    private void handleEnregistrerTraitement() {
        // Validation des champs
        if (nomCompletField.getText().isEmpty() || medecinField.getText().isEmpty() ||
                medicamentField.getText().isEmpty() || posologieField.getText().isEmpty() ||
                dateDebutPicker.getValue() == null) {
            showAlert("Validation", "Veuillez remplir tous les champs obligatoires", Alert.AlertType.WARNING);
            return;
        }

        try {
            if (traitementEnCours == null) {
                Traitement traitement = new Traitement();
                traitement.setNomComplet(nomCompletField.getText());
                traitement.setMedecin(medecinField.getText());
                traitement.setMedicament(medicamentField.getText());
                traitement.setPosologie(posologieField.getText());
                traitement.setDateDebut(dateDebutPicker.getValue());
                traitement.setDateFin(dateFinPicker.getValue());
                traitement.setInstructions(instructionsArea.getText());
                traitement.setEnCours(enCoursCheck.isSelected());

                traitementDAO.addTraitement(traitement);
                showAlert("Succès", "Traitement enregistré avec succès", Alert.AlertType.INFORMATION);
            }
            else{  traitementEnCours.setNomComplet(nomCompletField.getText());
                traitementEnCours.setMedecin(medecinField.getText());
                traitementEnCours.setMedicament(medicamentField.getText());
                traitementEnCours.setPosologie(posologieField.getText());
                traitementEnCours.setDateDebut(dateDebutPicker.getValue());
                traitementEnCours.setDateFin(dateFinPicker.getValue());
                traitementEnCours.setInstructions(instructionsArea.getText());
                traitementEnCours.setEnCours(enCoursCheck.isSelected());

                traitementDAO.updateTraitement(traitementEnCours);
                showAlert("Succès", "Traitement mis à jour avec succès", Alert.AlertType.INFORMATION);
            }
            loadTraitements();
            mainTabPane.getSelectionModel().select(listTab);
            formTab.setDisable(true);
            clearForm();
            traitementEnCours = null;

        } catch (SQLException e) {
            showAlert("Erreur", "Erreur lors de l'enregistrement", Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    @FXML
    private void handleAnnulerFormulaire() {
        clearForm();
        mainTabPane.getSelectionModel().select(listTab);
        formTab.setDisable(true);
    }


    private void handleEditTraitement(Traitement traitement) {
        this.traitementEnCours = traitement;
        nomCompletField.setText(traitement.getNomComplet());
        medecinField.setText(traitement.getMedecin());
        medicamentField.setText(traitement.getMedicament());
        posologieField.setText(traitement.getPosologie());
        dateDebutPicker.setValue(traitement.getDateDebut());
        dateFinPicker.setValue(traitement.getDateFin());
        instructionsArea.setText(traitement.getInstructions());
        enCoursCheck.setSelected(traitement.isEnCours());

        mainTabPane.getSelectionModel().select(formTab);
        formTab.setDisable(false);
    }
    // après update


    private void handleDeleteTraitement(Traitement traitement) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation de suppression");
        alert.setHeaderText("Supprimer le traitement");
        alert.setContentText("Êtes-vous sûr de vouloir supprimer ce traitement ?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                traitementDAO.deleteTraitement(traitement.getId());
                loadTraitements();
                showAlert("Succès", "Traitement supprimé avec succès", Alert.AlertType.INFORMATION);
            } catch (SQLException e) {
                showAlert("Erreur", "Erreur lors de la suppression", Alert.AlertType.ERROR);
                e.printStackTrace();
            }
        }
    }

    private void clearForm() {
        nomCompletField.clear();
        medecinField.clear();
        medicamentField.clear();
        posologieField.clear();
        dateDebutPicker.setValue(null);
        dateFinPicker.setValue(null);
        instructionsArea.clear();
        enCoursCheck.setSelected(false);
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
    private void goAccueil() throws IOException {
        loadScene("/views/HomePage.fxml","Home Page");
    }

    @FXML
    private void goPatients() throws IOException {
        loadScene("/views/PatientInterface.fxml","Patients");
    }

    @FXML
    private void goTraitements() throws IOException {
        loadScene("/views/Traitement.fxml","Traitements");
    }

    @FXML
    private void goStats() throws IOException {
        loadScene("/views/Statistiques.fxml","Statistiques");
    }

    @FXML
    private void goRendezVous() throws IOException {
        loadScene("/views/rdv.fxml","RendezVous");
    }

    @FXML
    private void goParametres() throws IOException {
        loadScene("/views/Parametres.fxml","Parametres");
    }

    private void loadScene(String fxmlPath,String title) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
        Stage stage = (Stage) mainTabPane.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle(title);
        stage.show();
    }
}