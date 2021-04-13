package be.vinci.pae.domain.furniture;

public class TypeOfFurnitureFactoryImpl implements TypeOfFurnitureFactory {

  @Override
  public TypeOfFurnitureDTO getTypeOfFurnitureDTO() {
    return new TypeOfFurnitureImpl();
  }

}
