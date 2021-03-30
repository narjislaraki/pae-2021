package be.vinci.pae.domain.furniture;

public class OptionFactoryImpl implements OptionFactory {

  @Override
  public OptionDTO getOptionDTO() {
    return new OptionImpl();
  }

}
