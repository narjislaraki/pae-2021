package be.vinci.pae.factories;

import be.vinci.pae.domain.OptionImpl;
import be.vinci.pae.domain.interfaces.OptionDTO;
import be.vinci.pae.factories.interfaces.OptionFactory;

public class OptionFactoryImpl implements OptionFactory {

  @Override
  public OptionDTO getOptionDTO() {
    return new OptionImpl();
  }

}
