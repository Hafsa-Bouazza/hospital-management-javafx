package controllers;

import dao.StatistiquesDAO;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;

public class StatistiquesController {

    @FXML private Label totalPatientsLabel;
    @FXML private Label moisChargeLabel;
    @FXML private Label moyenneMensuelleLabel;
    @FXML private BarChart<String, Number> barChart;

    @FXML private Button btnAccueil;
    @FXML private Button btnPatients;
    @FXML private Button btnTraitements;
    @FXML private Button btnStats;
    @FXML private Button btnRdv;
    @FXML private Button btnParametres;

    private final StatistiquesDAO statistiquesDAO = new StatistiquesDAO();

    @FXML
    public void initialize() {
        chargerStatistiques();
        configurerNavigation();
    }

    private void chargerStatistiques() {
        try {
            totalPatientsLabel.setText(String.valueOf(statistiquesDAO.getTotalPatients()));
            moisChargeLabel.setText(statistiquesDAO.getMoisPlusCharge());
            moyenneMensuelleLabel.setText(String.format("%.2f", statistiquesDAO.getMoyenneMensuelle()));

            XYChart.Series<String, Number> series = new XYChart.Series<>();
            for (Map.Entry<String, Integer> entry : statistiquesDAO.getPatientsParMois().entrySet()) {
                series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
            }
            barChart.getData().add(series);

        } catch (SQLException e) {
            System.err.println("Erreur lors du chargement des statistiques : " + e.getMessage());
        }
    }

    private void configurerNavigation() {
        btnAccueil.setOnAction(e -> chargerScene("/views/HomePage.fxml","Home Page"));
        btnPatients.setOnAction(e -> chargerScene("/views/PatientInterface.fxml","Patients"));
        btnTraitements.setOnAction(e -> chargerScene("/views/Traitement.fxml","Traitements"));
        btnStats.setOnAction(e -> chargerScene("/views/statistiques.fxml","Statistiques")); // Recharge cette page
        btnRdv.setOnAction(e -> chargerScene("/views/rdv.fxml","Rendez vous"));
        btnParametres.setOnAction(e -> chargerScene("/views/Parametres.fxml","Parametres"));
    }

    private void chargerScene(String fxmlPath,String title) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
            Stage stage = (Stage) btnAccueil.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle(title);
        } catch (IOException e) {
            System.err.println("Erreur lors du changement de scène : " + e.getMessage());
        }
    }
}
