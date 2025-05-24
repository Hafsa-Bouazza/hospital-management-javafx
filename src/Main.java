import dao.DatabaseConnection;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.sql.Connection;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Vérifie la connexion à la base de données
        Connection connection = DatabaseConnection.getConnection();

        if (connection != null) {
            System.out.println("Connexion à la base de données réussie !");
        } else {
            System.out.println("Échec de la connexion à la base de données.");
        }

        // Chargement du fichier FXML
        Parent root = FXMLLoader.load(getClass().getResource("views/Acceuil.fxml"));

        // Configuration et affichage de la scène
        primaryStage.setTitle("Accueil - Application Médicale");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
