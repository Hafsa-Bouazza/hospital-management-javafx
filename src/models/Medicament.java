
package models;

public class Medicament {
    private String nom;
    private String forme;
    private String dosage;
    private String effetSecodaires;
    
    public Medicament( String nom, 
                      String forme, String dosage,
                      String effetSecondaires) {
        this.nom = nom;
        this.forme = forme;
        this.dosage = dosage;
        this.effetSecodaires= effetSecondaires;
    }
    
   
     public String getNom() { return nom; }
    public void setNom(String nomCommercial) { this.nom = nom; }
    
       public String getForme() { return forme; }
    public void setForme(String forme) { this.forme = forme; }
    
     public String getDosage() { return dosage; }
    public void setDosage(String dosage) { this.dosage = dosage; }
    
     public String getEffetsSecondaires() { return effetSecodaires; }
    public void setEffetsSecondaires(String effetSecondaires) { this.effetSecodaires = effetSecondaires; }

    
    @Override
    public String toString() {
        return nom + " (" + dosage + ", " + forme + ")";
    }
}

