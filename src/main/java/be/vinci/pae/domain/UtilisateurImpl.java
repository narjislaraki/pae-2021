package be.vinci.pae.domain;

import java.time.LocalDateTime;

public class UtilisateurImpl implements Utilisateur {

  private int id, adresse;
  private String pseudo, nom, prenom, email, mot_de_passe;
  private Role role;
  private boolean est_valide;
  private LocalDateTime date_inscription; // TODO DateTime?

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
  public String getMot_de_passe() {
    return mot_de_passe;
  }

  @Override
  public void setMot_de_passe(String mot_de_passe) {
    this.mot_de_passe = mot_de_passe;
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
  public boolean isEst_valide() { // TODO Modifier?
    return est_valide;
  }

  @Override
  public void setEst_valide(boolean est_valide) {
    this.est_valide = est_valide;
  }

  @Override
  public LocalDateTime getDate_inscription() {
    return date_inscription;
  }

  @Override
  public void setDate_inscription(LocalDateTime date_inscription) {
    this.date_inscription = date_inscription;
  }

}
