package be.vinci.pae.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(as = UtilisateurImpl.class)
public interface Utilisateur extends UtilisateurDTO {

  boolean checkMotDePasse(String motDePasse);

  String hashMotDePasse(String motDePasse);


}
