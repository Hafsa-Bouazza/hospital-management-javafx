
package models;


public class Infermier extends Utilisateur{
     private String tache;

    public Infermier(int id,String nom,String prenom,String email,String password,String role,String tache){
        super(id,nom, prenom, email, password,role);
        this.tache=tache;
    }
    
    public String getTache(){
        return tache;
    }
    public void setTache(String t){
        tache=t;
    }
    
}
