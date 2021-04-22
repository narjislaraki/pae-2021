package be.vinci.pae.domain.visit;

import com.fasterxml.jackson.annotation.JsonView;
import be.vinci.pae.views.Views;

public class PhotoImpl implements PhotoDTO {

  @JsonView(Views.Public.class)
  private int id;
  @JsonView(Views.Public.class)
  private String photo;
  @JsonView(Views.Public.class)
  private boolean isVisible;
  @JsonView(Views.Public.class)
  private boolean isAClientPhoto;
  @JsonView(Views.Public.class)
  private int idFurniture;

  @Override
  public int getId() {
    return this.id;
  }

  @Override
  public void setId(int id) {
    this.id = id;
  }

  @Override
  public String getPhoto() {
    return this.photo;
  }

  @Override
  public void setPhoto(String photo) {
    this.photo = photo;
  }

  @Override
  public boolean isVisible() {
    return isVisible;
  }

  @Override
  public void setVisible(boolean isVisible) {
    this.isVisible = isVisible;
  }

  @Override
  public boolean isAClientPhoto() {
    return this.isAClientPhoto;
  }

  @Override
  public void setIsAClientPhoto(boolean isAClientPhoto) {
    this.isAClientPhoto = isAClientPhoto;
  }

  @Override
  public int getIdFurniture() {
    return this.idFurniture;
  }

  @Override
  public void setIdFurniture(int idFurniture) {
    this.idFurniture = idFurniture;
  }

  @Override
  public String toString() {
    return "PhotoImpl [id=" + id + ", photo=" + photo + ", isVisible=" + isVisible
        + ", isAClientPhoto=" + isAClientPhoto + ", idFurniture=" + idFurniture + "]";
  }
}
