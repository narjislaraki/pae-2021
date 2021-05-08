package be.vinci.pae.domain.interfaces;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import be.vinci.pae.domain.PhotoImpl;

@JsonDeserialize(as = PhotoImpl.class)
public interface PhotoDTO {

  int getId();

  void setId(int id);

  String getPhoto();

  void setPhoto(String photo);

  boolean isVisible();

  void setVisible(boolean isVisible);

  boolean isAClientPhoto();

  void setIsAClientPhoto(boolean isAClientPhoto);

  int getIdFurniture();

  void setIdFurniture(int idFurniture);

}
