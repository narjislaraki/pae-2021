package be.vinci.pae.domain;

import be.vinci.pae.services.UtilisateurDAO;

import jakarta.inject.Inject;

public class UtilisateurUCCImpl implements UtilisateurUCC {

  @Inject
  private UtilisateurDAO utilisateurDAO;


  @Override
  public Utilisateur connexion(String pseudo, String password) {
    // TODO
    return null;
  }
}
