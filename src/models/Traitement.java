
package models;
import java.time.LocalDate;

public class Traitement {
    private Patient patient;
    private Medecin prescripteur;
    private Medicament medicament;
    private String posologie;
    private LocalDate dateDebut;
    private LocalDate dateFin;
    private String instructions;
    private boolean enCours;
    
    public Traitement( Patient patient, Medecin prescripteur,
                      Medicament medicament, String posologie,
                      LocalDate dateDebut, LocalDate dateFin,
                      String instructions, boolean enCours) {
        
        this.patient = patient;
        this.prescripteur = prescripteur;
        this.medicament = medicament;
        this.posologie = posologie;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.instructions = instructions;
        this.enCours = enCours;
    }
    
    
   

    public Patient getPatient() { return patient; }
    public void setPatient(Patient patient) { this.patient = patient; }

    public Medecin getPrescripteur() { return prescripteur; }
    public void setPrescripteur(Medecin prescripteur) { this.prescripteur = prescripteur; }

    public Medicament getMedicament() { return medicament; }
    public void setMedicament(Medicament medicament) { this.medicament = medicament; }

    public String getPosologie() { return posologie; }
    public void setPosologie(String posologie) { this.posologie = posologie; }

    public LocalDate getDateDebut() { return dateDebut; }
    public void setDateDebut(LocalDate dateDebut) { this.dateDebut = dateDebut; }

    public LocalDate getDateFin() { return dateFin; }
    public void setDateFin(LocalDate dateFin) { this.dateFin = dateFin; }

    public String getInstructions() { return instructions; }
    public void setInstructions(String instructions) { this.instructions = instructions; }

    public boolean isEnCours() { return enCours; }
    public void setEnCours(boolean enCours) { this.enCours = enCours; }
    
    @Override
    public String toString() {
        return "Traitement de " + patient.getNom() + " - " + medicament.getNom();
    }
}
