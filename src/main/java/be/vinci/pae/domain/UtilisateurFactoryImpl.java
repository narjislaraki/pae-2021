package be.vinci.pae.domain;

public class UtilisateurFactoryImpl implements UtilisateurFactory {

  @Override
  public UtilisateurDTO getUtilisateurDTO() {
    return new UtilisateurImpl();
  }
}
