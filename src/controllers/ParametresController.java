package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import models.User;
import models.SessionManager;
import utils.ThemeManager;

import java.io.IOException;

public class ParametresController {

    @FXML
    private Label userNameLabel;
    @FXML
    private Label userRoleLabel;
    @FXML
    private Label userEmailLabel;
    @FXML
    private ToggleButton themeToggleBtn;
    @FXML
    private Label themeLabel;
    @FXML
    private VBox mainContentContainer;

    @FXML
    public void initialize() {
        User user = SessionManager.getCurrentUser();
        if (user != null) {
            userNameLabel.setText(user.getUsername());
            userEmailLabel.setText(user.getEmail());
            userRoleLabel.setText(user.getRole());
        }

        // Appliquer le thème actuel dès l’ouverture
        Scene scene = mainContentContainer.getScene();
        if (scene != null) {
            scene.getStylesheets().clear();
            scene.getStylesheets().add(getClass().getResource(ThemeManager.getCurrentThemeStylesheet()).toExternalForm());
        } else {
            // S'assure que le thème est appliqué après un petit délai si la scène n'est pas encore prête
            mainContentContainer.sceneProperty().addListener((obs, oldScene, newScene) -> {
                if (newScene != null) {
                    newScene.getStylesheets().clear();
                    newScene.getStylesheets().add(getClass().getResource(ThemeManager.getCurrentThemeStylesheet()).toExternalForm());
                }
            });
        }

        // Initialiser le toggle à l’état actuel du thème
        themeToggleBtn.setSelected(ThemeManager.isDarkMode());
        themeLabel.setText(ThemeManager.isDarkMode() ? "Mode Clair" : "Mode Sombre");
    }

    @FXML
    private void handleEditProfile(ActionEvent event) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Vérification");
        dialog.setHeaderText("Confirmer votre mot de passe");
        dialog.setContentText("Mot de passe :");

        dialog.showAndWait().ifPresent(password -> {
            if (SessionManager.verifyPassword(password)) {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/EditProfile.fxml"));
                    Parent root = loader.load();

                    EditProfileController controller = loader.getController();
                    controller.prefillForm(SessionManager.getCurrentUser());

                    Stage stage = new Stage();
                    stage.setScene(new Scene(root));
                    stage.initModality(Modality.APPLICATION_MODAL);
                    stage.setTitle("Modifier le profil");
                    stage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Mot de passe incorrect.");
                alert.showAndWait();
            }
        });
    }

    @FXML
    private void handleLogout(ActionEvent event) {
        SessionManager.clearSession();
        SceneNavigator.switchScene("/views/Connexion.fxml", "Connexion", event);
    }

    @FXML
    private void handleAccueil(ActionEvent event) {
        SceneNavigator.switchScene("/views/HomePage.fxml", "Home Page", event);
    }

    @FXML
    private void handlePatients(ActionEvent event) {
        SceneNavigator.switchScene("/views/PatientInterface.fxml", "Patients", event);
    }

    @FXML
    private void handleTraitements(ActionEvent event) {
        SceneNavigator.switchScene("/views/Traitement.fxml", "Traitements", event);
    }

    @FXML
    private void handleStats(ActionEvent event) {
        SceneNavigator.switchScene("/views/statistiques.fxml", "Statistiques", event);
    }

    @FXML
    private void handleRdv(ActionEvent event) {
        SceneNavigator.switchScene("/views/rdv.fxml", "Rendez Vous", event);
    }

    @FXML
    private void handleParametres(ActionEvent event) {
        SceneNavigator.switchScene("/views/Parametres.fxml", "Parametres", event);
    }

    // ========== THEME CLAIR/SOMBRE ==========

    @FXML
    private void handleThemeToggle(ActionEvent event) {
        boolean isDark = themeToggleBtn.isSelected();

        // Sauvegarder l’état du thème
        ThemeManager.setDarkMode(isDark);

        // Appliquer le bon CSS
        String themeCss = ThemeManager.getCurrentThemeStylesheet();
        Scene scene = mainContentContainer.getScene();
        if (scene != null) {
            scene.getStylesheets().clear();
            scene.getStylesheets().add(getClass().getResource(themeCss).toExternalForm());
        }

        // Mettre à jour le texte de l’étiquette
        themeLabel.setText(isDark ? "Mode Clair" : "Mode Sombre");
    }

    // Autres handlers
    @FXML
    private void handleManageAccount(ActionEvent event) {
        // à implémenter
    }

    @FXML
    private void handleChangePassword(ActionEvent event) {
        // à implémenter
    }

    @FXML
    private void handleConfigureNotifications(ActionEvent event) {
        // à implémenter
    }

    @FXML
    private void handleLanguageChange(ActionEvent event) {
        ComboBox<String> comboBox = (ComboBox<String>) event.getSource();
        String selected = comboBox.getValue();
        // à implémenter
    }
}
