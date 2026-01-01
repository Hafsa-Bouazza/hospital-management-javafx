package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import models.User;
import dao.UpdateUserDAO;

public class EditProfileController {

    @FXML private TextField prenomField;
    @FXML private TextField nomField;
    @FXML private TextField emailField;

    private User currentUser;

    // Pré-remplit le formulaire avec les données de l'utilisateur
    public void prefillForm(User user) {
        this.currentUser = user;

        // Si tu stockes nom et prénom dans username, on les découpe ici
        if (user.getUsername() != null && user.getUsername().contains(" ")) {
            String[] parts = user.getUsername().split(" ", 2);
            prenomField.setText(parts[0]);
            nomField.setText(parts[1]);
        } else {
            prenomField.setText(user.getUsername()); // ou rien
            nomField.setText("");
        }

        emailField.setText(user.getEmail());
    }

    @FXML
    private void handleSave() {
        String prenom = prenomField.getText().trim();
        String nom = nomField.getText().trim();
        String email = emailField.getText().trim();

        if (prenom.isEmpty() || nom.isEmpty() || email.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Tous les champs sont obligatoires.");
            return;
        }

        String newUsername = prenom + " " + nom;
        currentUser.setUsername(newUsername);
        currentUser.setEmail(email);

        boolean updated = UpdateUserDAO.updateUser(currentUser);

        if (updated) {
            showAlert(Alert.AlertType.INFORMATION, "Profil mis à jour avec succès.");
            closeWindow();
        } else {
            showAlert(Alert.AlertType.ERROR, "Échec de la mise à jour du profil.");
        }
    }

    private void closeWindow() {
        Stage stage = (Stage) prenomField.getScene().getWindow();
        stage.close();
    }

    private void showAlert(Alert.AlertType type, String message) {
        Alert alert = new Alert(type);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
