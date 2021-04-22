package be.vinci.pae.domain.visit;

public interface PhotoDTO {

  int getId();

  void setId(int id);

  byte[] getPhoto();

  void setPhoto(byte[] photo);

  boolean isVisible();

  void setVisible(boolean isVisible);

  boolean isAClientPhoto();

  void setIsAClientPhoto(boolean isAClientPhoto);

  int getIdFurniture();

  void setIdFurniture(int idFurniture);

}
