package be.vinci.pae.domain;

public interface Utilisateur extends UtilisateurDTO {

  boolean checkMotDePasse(String motDePasse);

  String hashMotDePasse(String motDePasse);


}
