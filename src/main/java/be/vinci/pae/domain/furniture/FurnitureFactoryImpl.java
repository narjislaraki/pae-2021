package be.vinci.pae.domain.furniture;

public class FurnitureFactoryImpl implements FurnitureFactory {

  @Override
  public FurnitureDTO getFurnitureDTO() {
    return new FurnitureImpl();
  }

}
