package models;
import java.time.*;
public class Patient {
    private int id;
    private String nom;
    private String prenom;
    private LocalDate dateNaissance;
    private String sexe;
    private String telephone;
    private int numeroDossier;
    
     public Patient(int id, String nom, String prenom, LocalDate dateNaissance, String sexe,
                   String telephone, int numeroDossier) {
         this.id=id;
        this.nom = nom;
        this.prenom = prenom;
        this.dateNaissance = dateNaissance;
        this.sexe = sexe;
        this.telephone = telephone;
        this.numeroDossier = numeroDossier;
        
    }
     public int getId() { return id; }
     public void setId(int id) { this.id = id; }
     
     public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    
    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }
    
     public LocalDate getDateNaissance() { return dateNaissance; }
    public void setDateNaissance(LocalDate dateNaissance) { this.dateNaissance = dateNaissance; }
    
    public String getSexe() { return sexe; }
    public void setSexe(String sexe) { this.sexe = sexe; }
    
      public String getTelephone() { return telephone; }
    public void setTelephone(String telephone) { this.telephone = telephone; }
    
    public int getNumeroDossier() { return numeroDossier; }
    public void setNumeroDossier(int numeroDossier) { this.numeroDossier = numeroDossier; }
 
    
}
