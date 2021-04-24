package be.vinci.pae.domain.visit;

public class PhotoFactoryImpl implements PhotoFactory {

  @Override
  public PhotoDTO getPhotoDTO() {
    return new PhotoImpl();
  }

}
