package be.vinci.pae.domain;

public class UtilisateurFactoryImpl implements UtilisateurFactory {

  @Override
  public Utilisateur getUtilisateur() {
    return new UtilisateurImpl();
  }
}
