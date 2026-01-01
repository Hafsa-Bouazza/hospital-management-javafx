package controllers;

import dao.UserDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import models.SessionManager;
import models.User;

import java.io.IOException;

public class ConnexionController {

    // Éléments FXML
    @FXML
    private Label labelConnexion;
    @FXML
    private Label subtitleLabel;
    @FXML
    private TextField usernamefield;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Button SeconnecterBtn;
    @FXML
    private Hyperlink passwordLink;
    @FXML
    private Hyperlink Inscription;
    @FXML
    private Label errorLabel;

    private final UserDAO userDAO = new UserDAO();

    // Méthode de connexion
    @FXML
    private void handleConnexion(ActionEvent event) {
        String email = usernamefield.getText();
        String password = passwordField.getText();

        if (email.isEmpty() || password.isEmpty()) {
            showError("Veuillez remplir tous les champs");
            return;
        }

        try {
            User user = userDAO.authenticate(email, password);
            if (user != null) {
                SessionManager.setCurrentUser(user);
                System.out.println("Utilisateur authentifié : " + user);
                SessionManager.setCurrentUser(user);
                System.out.println("Utilisateur dans SessionManager après set : " + SessionManager.getCurrentUser());

                loadHomePage();
            } else {
                showError("Identifiant ou mot de passe incorrect");
            }


        } catch (Exception e) {
            showError("Erreur de connexion: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Méthode pour l'inscription
    @FXML
    private void handleCreerCompte(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/views/Inscription.fxml"));
            Stage stage = (Stage) Inscription.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Créer un compte");
        } catch (IOException e) {
            showError("Impossible d'ouvrir la page d'inscription");
            e.printStackTrace();
        }
    }

    // Méthode pour mot de passe oublié
    @FXML
    private void handlePasswordForgotten(ActionEvent event) {
        showInfo("Un email de réinitialisation a été envoyé");
        // Implémentez ici la logique de réinitialisation
    }

    // Chargement de la page d'accueil
    private void loadHomePage() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/views/HomePage.fxml"));
        Stage stage = (Stage) SeconnecterBtn.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle("Home Page");
    }

    // Affichage des messages d'erreur
    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setStyle("-fx-text-fill: red;");
    }

    private void showInfo(String message) {
        errorLabel.setText(message);
        errorLabel.setStyle("-fx-text-fill: green;");
    }
}