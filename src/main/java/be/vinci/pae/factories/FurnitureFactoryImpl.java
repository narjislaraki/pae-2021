package be.vinci.pae.factories;

import be.vinci.pae.domain.FurnitureImpl;
import be.vinci.pae.domain.interfaces.FurnitureDTO;
import be.vinci.pae.factories.interfaces.FurnitureFactory;

public class FurnitureFactoryImpl implements FurnitureFactory {

  @Override
  public FurnitureDTO getFurnitureDTO() {
    return new FurnitureImpl();
  }

}
