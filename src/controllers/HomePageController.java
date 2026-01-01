package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class HomePageController {

    @FXML
    private Button btnAccueil, btnPatients, btnTraitements, btnStats, btnRdv, btnParametres;

    @FXML
    private void handleAccueil(ActionEvent event) {
        chargerNouvelleScene(event, "HomePage.fxml", "Accueil");
    }

    @FXML
    private void handlePatients(ActionEvent event) {
        chargerNouvelleScene(event, "PatientInterface.fxml", "Patients");
    }

    @FXML
    private void handleTraitements(ActionEvent event) {
        chargerNouvelleScene(event, "Traitement.fxml", "Traitements");
    }

    @FXML
    private void handleStats(ActionEvent event) {
        chargerNouvelleScene(event, "statistiques.fxml", "Statistiques");
    }

    @FXML
    private void handleRdv(ActionEvent event) {
        chargerNouvelleScene(event, "rdv.fxml", "Rendez-vous");
    }

    @FXML
    private void handleParametres(ActionEvent event) {
        chargerNouvelleScene(event, "Parametres.fxml", "Paramètres");
    }

    // Méthode utilitaire pour charger une nouvelle scène
    private void chargerNouvelleScene(ActionEvent event, String fichierFXML, String titreFenetre) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("../views/" + fichierFXML));
            //Parent root = FXMLLoader.load(getClass().getResource("/views/" + fichierFXML));
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle(titreFenetre);
            stage.show();
        } catch (IOException e) {
            showError("Impossible d'ouvrir la page : " + fichierFXML);
            e.printStackTrace();
        }
    }

    private void showError(String message) {
        System.out.println("[ERREUR] " + message);
        // Tu peux ajouter une boîte de dialogue si tu veux (ex: Alert)
    }
}
