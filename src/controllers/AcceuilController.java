package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

public class AcceuilController {

    @FXML
    private TextField searchField;

    @FXML
    private Button searchButton;

    @FXML
    private Button dentiste;

    @FXML
    private Button psychiatre;

    @FXML
    private Button cardiologue;

    @FXML
    private Button neurologie;

    @FXML
    private Button dermatologie;

    @FXML
    private Button orthopedie;

    @FXML
    private Button btnEspaceSecretaire;

    @FXML
    private Button btnEspaceInfermiere;

    @FXML
    private Label labelBienvenue;

    // Méthode appelée automatiquement après le chargement du FXML
    @FXML
    public void initialize() {
        // Ajout d'exemple de comportement
        searchButton.setOnAction(e -> handleSearch());
        btnEspaceSecretaire.setOnAction(e -> openConnexionSecretaire());
        btnEspaceInfermiere.setOnAction(e -> openConnexionInfirmiere());

        // Liens boutons spécialités
        dentiste.setOnAction(e -> openSpecialite("Dentiste"));
        psychiatre.setOnAction(e -> openSpecialite("Psychiatre"));
        cardiologue.setOnAction(e -> openSpecialite("Cardiologue"));
        neurologie.setOnAction(e -> openSpecialite("Neurologue"));
        dermatologie.setOnAction(e -> openSpecialite("Dermatologue"));
        orthopedie.setOnAction(e -> openSpecialite("Orthopédie"));
    }

    private void handleSearch() {
        String query = searchField.getText().trim();
        System.out.println("Recherche : " + query);
        // Ici, tu peux filtrer les boutons ou afficher un résultat.
    }

    private void openConnexionSecretaire() {
        System.out.println("Connexion à l'espace secrétaire");
        // Charger une nouvelle scène, etc.
    }

    private void openConnexionInfirmiere() {
        System.out.println("Connexion à l'espace infirmière");
        // Charger une nouvelle scène, etc.
    }

    private void openSpecialite(String nom) {
        System.out.println("Spécialité sélectionnée : " + nom);
        // Ouvre une page dédiée à la spécialité
    }
}
