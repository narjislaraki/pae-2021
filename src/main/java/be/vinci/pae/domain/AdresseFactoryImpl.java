package be.vinci.pae.domain;

public class AdresseFactoryImpl implements AdresseFactory {

  @Override
  public Utilisateur getAdresse() {
    return new UtilisateurImpl();
  }
}
