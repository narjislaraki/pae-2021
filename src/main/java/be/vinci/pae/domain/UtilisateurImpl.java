package be.vinci.pae.domain;

import java.time.LocalDateTime;

public class UtilisateurImpl implements Utilisateur {

  private int id;
  private int adresse;
  private String pseudo;
  private String nom;
  private String prenom;
  private String email;
  private String motDePasse;
  private Role role;
  private boolean estValide;
  private LocalDateTime dateInscription; // TODO DateTime?

  @Override
  public int getId() {
    return id;
  }

  @Override
  public void setId(int id) {
    this.id = id;
  }

  @Override
  public int getAdresse() {
    return adresse;
  }

  @Override
  public void setAdresse(int adresse) {
    this.adresse = adresse;
  }

  @Override
  public String getPseudo() {
    return pseudo;
  }

  @Override
  public void setPseudo(String pseudo) {
    this.pseudo = pseudo;
  }

  @Override
  public String getNom() {
    return nom;
  }

  @Override
  public void setNom(String nom) {
    this.nom = nom;
  }

  @Override
  public String getPrenom() {
    return prenom;
  }

  @Override
  public void setPrenom(String prenom) {
    this.prenom = prenom;
  }

  @Override
  public String getEmail() {
    return email;
  }

  @Override
  public void setEmail(String email) {
    this.email = email;
  }

  @Override
  public String getMotDePasse() {
    return motDePasse;
  }

  @Override
  public void setMotDePasse(String motDePasse) {
    this.motDePasse = motDePasse;
  }

  @Override
  public Role getRole() {
    return role;
  }

  @Override
  public void setRole(String role) {
    switch (role.toLowerCase()) {
      case "admin":
        this.role = Role.ADMIN;
        break;

      case "antiquaire":
        this.role = Role.ANTIQUAIRE;
        break;

      case "client":
        this.role = Role.CLIENT;
        break;

      default:
        throw new IllegalArgumentException();
    }
  }

  @Override
  public boolean isEstValide() { // TODO Modifier?
    return estValide;
  }

  @Override
  public void setEstValide(boolean estValide) {
    this.estValide = estValide;
  }

  @Override
  public LocalDateTime getDateInscription() {
    return dateInscription;
  }

  @Override
  public void setDateInscription(LocalDateTime dateInscription) {
    this.dateInscription = dateInscription;
  }

}
