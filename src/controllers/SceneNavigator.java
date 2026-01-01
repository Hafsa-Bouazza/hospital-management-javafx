package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import utils.ThemeManager;

import java.io.IOException;

public class SceneNavigator {

    public static void switchScene(String fxmlPath, String title, ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(SceneNavigator.class.getResource(fxmlPath));
            Parent root = loader.load();

            // Créer une nouvelle scène
            Scene scene = new Scene(root);

            // Appliquer le thème clair ou sombre
            String themeCss = ThemeManager.getCurrentThemeStylesheet();
            scene.getStylesheets().add(SceneNavigator.class.getResource(themeCss).toExternalForm());

            // Appliquer la scène à la fenêtre
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle(title);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
