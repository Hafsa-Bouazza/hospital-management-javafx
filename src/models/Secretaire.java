package models;

public class Secretaire extends Utilisateur{
    
    private String service;
     
   public Secretaire(int id,String nom,String prenom,String email,String password,String role,String service){
     super(id,nom,prenom,email,password,role);
     this.service=service;
}
    public String getService(){
        return service;
    }
    public void setService(String s){
        service=s;
    }
}
