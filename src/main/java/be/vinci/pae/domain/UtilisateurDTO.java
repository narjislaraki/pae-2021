package be.vinci.pae.domain;

import java.time.LocalDateTime;


public interface UtilisateurDTO {

  enum Role {
    ADMIN, ANTIQUAIRE, CLIENT
  }

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

  String getMotDePasse();

  void setMotDePasse(String motDePasse);

  Role getRole();

  void setRole(String role);

  boolean isEstValide();

  void setEstValide(boolean estValide);

  LocalDateTime getDateInscription();

  void setDateInscription(LocalDateTime dateInscription);

}
