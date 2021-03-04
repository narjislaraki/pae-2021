package be.vinci.pae.domain;

import be.vinci.pae.services.UtilisateurDAO;

import jakarta.inject.Inject;

public class UtilisateurUCCImpl implements UtilisateurUCC {


  @Inject
  private UtilisateurDAO utilisateurDAO;



  @Override
  public UtilisateurDTO connexion(String pseudo) {
    UtilisateurDTO uDTO = utilisateurDAO.getUtilisateur(pseudo);
    return uDTO;
  }
}
