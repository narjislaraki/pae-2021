package be.vinci.pae.domain.edition;

import java.util.List;

import be.vinci.pae.domain.visit.PhotoDTO;

public class EditionImpl implements EditionDTO {

  private int idFurniture;
  private String description;
  private int idType;
  private double offeredSellingPrice;
  private int favouritePhotoId;
  private List<PhotoDTO> photosToAdd;
  private List<Integer> photosToDelete;
  private List<Integer> photosToDisplay;
  private List<Integer> photosToHide;

  @Override
  public int getIdFurniture() {
    return idFurniture;
  }

  @Override
  public void setIdFurniture(int idFurniture) {
    this.idFurniture = idFurniture;
  }

  @Override
  public String getDescription() {
    return description;
  }

  @Override
  public void setDescription(String description) {
    this.description = description;
  }

  @Override
  public int getIdType() {
    return idType;
  }

  @Override
  public void setIdType(int idType) {
    this.idType = idType;
  }

  @Override
  public double getOfferedSellingPrice() {
    return offeredSellingPrice;
  }

  @Override
  public void setOfferedSellingPrice(double offeredSellingPrice) {
    this.offeredSellingPrice = offeredSellingPrice;
  }

  @Override
  public int getFavouritePhotoId() {
    return favouritePhotoId;
  }

  @Override
  public void setFavouritePhotoId(int favouritePhotoId) {
    this.favouritePhotoId = favouritePhotoId;
  }

  @Override
  public List<PhotoDTO> getPhotosToAdd() {
    return photosToAdd;
  }

  @Override
  public void setPhotosToAdd(List<PhotoDTO> photoToAdd) {
    this.photosToAdd = photoToAdd;
  }

  @Override
  public List<Integer> getPhotosToDelete() {
    return photosToDelete;
  }

  @Override
  public void setPhotosToDelete(List<Integer> photoToDelete) {
    this.photosToDelete = photoToDelete;
  }

  @Override
  public List<Integer> getPhotosToDisplay() {
    return photosToDisplay;
  }

  @Override
  public void setPhotosToDisplay(List<Integer> photoToDisplay) {
    this.photosToDisplay = photoToDisplay;
  }

  @Override
  public List<Integer> getPhotosToHide() {
    return photosToHide;
  }

  @Override
  public void setPhotosToHide(List<Integer> photoToHide) {
    this.photosToHide = photoToHide;
  }

  @Override
  public String toString() {
    return "EditionImpl [idFurniture=" + idFurniture + ", description=" + description + ", idType="
        + idType + ", offeredSellingPrice=" + offeredSellingPrice + ", favouritePhotoId="
        + favouritePhotoId + ", photosToAdd=" + photosToAdd + ", photosToDelete=" + photosToDelete
        + ", photosToDisplay=" + photosToDisplay + ", photosToHide=" + photosToHide + "]";
  }

}
