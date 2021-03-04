package be.vinci.pae.services;

import be.vinci.pae.domain.Utilisateur;

import java.sql.Connection;

public class UtilisateurDAOImpl implements UtilisateurDAO {
  
  public UtilisateurDAOImpl() {
    // TODO Attention, la connexion devra se faire dans une classe externe


  }

  /**
   * Recherche de l'utilisateur dans la base de données via son pseudo.
   * 
   * @param pseudo le pseudo
   * @return l'utilisateur complet s'il existe, sinon null
   */
  @Override
  public Utilisateur getUtilisateur(String pseudo) {
    // TODO Auto-generated method stub
    return null;
  }

  /**
   * Recherche de l'utilisateur dans la base de données via son id.
   * 
   * @param id l'id
   * @return l'utilisateur complet s'il existe, sinon null
   */
  @Override
  public Utilisateur getUtilisateur(int id) {
    // TODO Auto-generated method stub
    return null;
  }


  /**
   * Ajoute un utilisateur dans la base de données.
   * 
   * @param utilisateur l'utilisateur
   */
  @Override
  public void addUtilisateur(Utilisateur utilisateur) {
    // TODO Auto-generated method stub --> return quelque chose type boolean ou plus complexe?
  }



}
