package be.vinci.pae.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(as = UtilisateurUCC.class)
public interface UtilisateurUCC {

  UtilisateurDTO connexion(String pseudo);

}
