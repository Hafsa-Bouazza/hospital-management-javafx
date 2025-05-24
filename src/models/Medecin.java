
package models;

public class Medecin extends Utilisateur{
     private String specialite;

    public Medecin(int id,String nom,String prenom,String email,String password,String role,String specialite){
        super(id,nom, prenom, email, password, role);
        this.specialite=specialite;
    }
    
    public String getSpecialite(){
        return specialite;
    }
    public void setSpecialite(String s){
        specialite=s;
    }
}
    
