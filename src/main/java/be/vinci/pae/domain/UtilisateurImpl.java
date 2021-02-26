package be.vinci.pae.domain;

import java.time.LocalDateTime;

public class UtilisateurImpl {

  enum Role {
    ADMIN, ANTIQUAIRE, CLIENT
  };

  private int id, adresse;
  private String pseudo, nom, prenom, email, mot_de_passe;
  private Role role;
  private boolean est_valide;
  private LocalDateTime date_inscription; // TODO DateTime?

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public int getAdresse() {
    return adresse;
  }

  public void setAdresse(int adresse) {
    this.adresse = adresse;
  }

  public String getPseudo() {
    return pseudo;
  }

  public void setPseudo(String pseudo) {
    this.pseudo = pseudo;
  }

  public String getNom() {
    return nom;
  }

  public void setNom(String nom) {
    this.nom = nom;
  }

  public String getPrenom() {
    return prenom;
  }

  public void setPrenom(String prenom) {
    this.prenom = prenom;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getMot_de_passe() {
    return mot_de_passe;
  }

  public void setMot_de_passe(String mot_de_passe) {
    this.mot_de_passe = mot_de_passe;
  }

  public Role getRole() {
    return role;
  }

  public void setRole(Role role) {
    this.role = role;
  }

  public boolean isEst_valide() {
    return est_valide;
  }

  public void setEst_valide(boolean est_valide) {
    this.est_valide = est_valide;
  }

  public LocalDateTime getDate_inscription() {
    return date_inscription;
  }

  public void setDate_inscription(LocalDateTime date_inscription) {
    this.date_inscription = date_inscription;
  }

}
