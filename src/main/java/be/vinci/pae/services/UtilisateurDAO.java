package be.vinci.pae.services;

import be.vinci.pae.domain.Utilisateur;
import be.vinci.pae.domain.UtilisateurDTO;


public interface UtilisateurDAO {

  UtilisateurDTO getUtilisateur(String pseudo);

  Utilisateur getUtilisateur(int id);

  void addUtilisateur(Utilisateur utilisateur);

}
