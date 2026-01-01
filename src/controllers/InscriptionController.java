package controllers;

import dao.UserDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import models.User;
import javafx.stage.Stage;

import java.io.IOException;

public class InscriptionController {

    @FXML
    private TextField nomField;
    @FXML
    private TextField prenomField;
    @FXML
    private TextField emailField;
    @FXML
    private PasswordField motDePasseField;
    @FXML
    private PasswordField confirmMotDePasseField;
    @FXML
    private Label errorLabel;

    private final UserDAO userDAO = new UserDAO();

    @FXML
    private void handleInscription(ActionEvent event) {
        // Validation des champs
        if (nomField.getText().isEmpty() || prenomField.getText().isEmpty() ||
                emailField.getText().isEmpty() || motDePasseField.getText().isEmpty()) {
            showError("Tous les champs sont obligatoires");
            return;
        }

        if (!motDePasseField.getText().equals(confirmMotDePasseField.getText())) {
            showError("Les mots de passe ne correspondent pas");
            return;
        }

        // if (!emailField.getText().matches("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
           // showError("Format d'email invalide");
            //return;
        //}

        // Création de l'utilisateur
        User newUser = new User();
        newUser.setUsername(nomField.getText() + " " + prenomField.getText());
        newUser.setEmail(emailField.getText());
        newUser.setRole("medecin"); // Rôle par défaut

        try {
            if (userDAO.register(newUser, motDePasseField.getText())) {
                showSuccess("Inscription réussie !");
                // Redirection après 2 secondes
                new java.util.Timer().schedule(
                        new java.util.TimerTask() {
                            @Override
                            public void run() {
                                javafx.application.Platform.runLater(() -> {
                                    try {
                                        loadConnexionPage();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                });
                            }
                        },
                        2000
                );
            } else {
                showError("L'email est déjà utilisé");
            }
        } catch (Exception e) {
            showError("Erreur lors de l'inscription");
            e.printStackTrace();
        }
    }

    @FXML
    private void handleRetourConnexion(ActionEvent event) throws IOException {
        loadConnexionPage();
    }

    private void loadConnexionPage() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/views/Connexion.fxml"));
        Stage stage = (Stage) nomField.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle("Connexion");
    }

    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setStyle("-fx-text-fill: red;");
    }

    private void showSuccess(String message) {
        errorLabel.setText(message);
        errorLabel.setStyle("-fx-text-fill: green;");
    }
}