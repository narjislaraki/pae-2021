package be.vinci.pae.factories;

import be.vinci.pae.domain.PhotoImpl;
import be.vinci.pae.domain.interfaces.PhotoDTO;
import be.vinci.pae.factories.interfaces.PhotoFactory;

public class PhotoFactoryImpl implements PhotoFactory {

  @Override
  public PhotoDTO getPhotoDTO() {
    return new PhotoImpl();
  }

}
