package be.vinci.pae.domain;


import java.time.LocalDateTime;


public interface Utilisateur {

  public enum Role {
    ADMIN, ANTIQUAIRE, CLIENT
  };

  int getId();

  void setId(int id);

  int getAdresse();

  void setAdresse(int adresse);

  String getPseudo();

  void setPseudo(String pseudo);

  String getNom();

  void setNom(String nom);

  String getPrenom();

  void setPrenom(String prenom);

  String getEmail();

  void setEmail(String email);

  String getMot_de_passe();

  void setMot_de_passe(String mot_de_passe);

  Role getRole();

  void setRole(String role);

  boolean isEst_valide();

  void setEst_valide(boolean est_valide);

  LocalDateTime getDate_inscription();

  void setDate_inscription(LocalDateTime date_inscription);

}
