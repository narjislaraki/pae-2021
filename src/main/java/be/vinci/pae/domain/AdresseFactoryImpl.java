package be.vinci.pae.domain;

public class AdresseFactoryImpl implements AdresseFactory {

  @Override
  public AdresseImpl getAdresse() {
    return new AdresseImpl();
  }
}
