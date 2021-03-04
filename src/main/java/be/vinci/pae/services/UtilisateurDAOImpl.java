package be.vinci.pae.services;

import be.vinci.pae.domain.Utilisateur;
import be.vinci.pae.domain.UtilisateurDTO;
import be.vinci.pae.domain.UtilisateurFactory;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import jakarta.inject.Inject;

public class UtilisateurDAOImpl implements UtilisateurDAO {

  @Inject
  private DalServices ds;

  @Inject
  private UtilisateurFactory uf;

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
  public UtilisateurDTO getUtilisateur(String pseudo) {
    // TODO PS -> attribut?
    // TODO retrait astérisque
    // TODO fetch de l'adresse aussi
    UtilisateurDTO u = null;

    try {
      PreparedStatement ps =
          ds.getPreparedStatement("SELECT * FROM pae.utilisateurs u WHERE u.pseudo = ?;");
      // u.id_personne, u.pseudo, u.nom, u.prenom, u.email, u.role, u.date_inscription, u.est_valide, u.mot_de_passe

      ps.setString(1, pseudo);

      ResultSet rs = ps.executeQuery();

      while (rs.next()) {
        u = uf.getUtilisateurDTO(); // TODO bon endroit?
        u.setId(rs.getInt(1));
        u.setPseudo(rs.getString(2));
        u.setNom(rs.getString(3));
        u.setPrenom(rs.getString(4));
        u.setEmail(rs.getString(5));
        u.setRole(rs.getString(6));
        u.setDateInscription(rs.getTimestamp(7).toLocalDateTime());
        u.setEstValide(rs.getBoolean(8));
        u.setMotDePasse(rs.getString(9));
        u.setAdresse(rs.getInt(10));
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return u;
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
