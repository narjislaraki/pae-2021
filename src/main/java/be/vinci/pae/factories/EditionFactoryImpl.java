package be.vinci.pae.factories;

import be.vinci.pae.domain.EditionImpl;
import be.vinci.pae.domain.interfaces.EditionDTO;
import be.vinci.pae.factories.interfaces.EditionFactory;

public class EditionFactoryImpl implements EditionFactory {

  @Override
  public EditionDTO getEditionDTO() {
    return new EditionImpl();
  }
}
