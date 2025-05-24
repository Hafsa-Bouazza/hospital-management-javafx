
package models;

public class Utilisateur {
    protected int id;
    protected String nom;
    protected String prenom;
    protected String email;
    protected String motdepasse;
    protected String role;

  public Utilisateur(int id,String nom,String prenom,String email,String motdepasse,String role){
     this.id=id;
     this.nom=nom;
     this.prenom=prenom;
     this.email=email;
     this.motdepasse=motdepasse;
    this.role=role;
  }
   
 public int getId() { return id; }
public void setId(int id) { this.id = id; }

  public String getNom(){
    return nom;
  }
  public void setNom(String n){
    nom=n;
  }
   public String getEmail(){
    return email;
  }
  public void setEmail(String e){
    email=e;
  }
  public String getPrenom(){
    return prenom;
  }
  public void setPrenom(String p){
    prenom=p;
  }
   public String getPassword(){
    return motdepasse;
  }
  public void setPassword(String ps){
   motdepasse=ps;
  }
public String getRole(){
    return role;
  }
  public void setRole(String r){
    role=r;
  }



  }


