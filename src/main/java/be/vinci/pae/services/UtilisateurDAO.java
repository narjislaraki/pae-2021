package be.vinci.pae.services;

import be.vinci.pae.domain.Utilisateur;


public interface UtilisateurDAO {

  Utilisateur getUtilisateur(String pseudo);

  Utilisateur getUtilisateur(int id);

  void addUtilisateur(Utilisateur utilisateur);

}
