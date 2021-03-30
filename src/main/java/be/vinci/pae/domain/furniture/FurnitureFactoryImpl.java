package be.vinci.pae.domain.furniture;

public class FurnitureFactoryImpl implements FurnitureFactory {

  @Override
  public FurnitureDTO getFurnitureDTO() {
    // TODO Auto-generated method stub
    return new FurnitureImpl();
  }

}
