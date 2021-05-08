package be.vinci.pae.factories;

import be.vinci.pae.domain.TypeOfFurnitureImpl;
import be.vinci.pae.domain.interfaces.TypeOfFurnitureDTO;
import be.vinci.pae.factories.interfaces.TypeOfFurnitureFactory;

public class TypeOfFurnitureFactoryImpl implements TypeOfFurnitureFactory {

  @Override
  public TypeOfFurnitureDTO getTypeOfFurnitureDTO() {
    return new TypeOfFurnitureImpl();
  }

}
