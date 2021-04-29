package be.vinci.pae.domain.edition;

import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import be.vinci.pae.domain.visit.PhotoDTO;

@JsonDeserialize(as = EditionImpl.class)
public interface EditionDTO {

  int getIdFurniture();

  void setIdFurniture(int idFurniture);

  String getDescription();

  void setDescription(String description);

  int getIdType();

  void setIdType(int idType);

  double getOfferedSellingPrice();

  void setOfferedSellingPrice(double offeredSellingPrice);

  int getFavouritePhotoId();

  void setFavouritePhotoId(int favouritePhotoId);

  List<PhotoDTO> getPhotosToAdd();

  void setPhotosToAdd(List<PhotoDTO> photoToAdd);

  List<Integer> getPhotosToDelete();

  void setPhotosToDelete(List<Integer> photoToDelete);

  List<Integer> getPhotosToDisplay();

  void setPhotosToDisplay(List<Integer> photoToDisplay);

  List<Integer> getPhotosToHide();

  void setPhotosToHide(List<Integer> photoToHide);

}
