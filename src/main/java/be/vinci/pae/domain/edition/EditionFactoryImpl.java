package be.vinci.pae.domain.edition;

public class EditionFactoryImpl implements EditionFactory {

  @Override
  public EditionDTO getEditionDTO() {
    return new EditionImpl();
  }
}
