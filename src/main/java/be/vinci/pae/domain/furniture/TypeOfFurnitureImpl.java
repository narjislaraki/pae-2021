package be.vinci.pae.domain.furniture;

import com.fasterxml.jackson.annotation.JsonView;
import be.vinci.pae.views.Views;

public class TypeOfFurnitureImpl implements TypeOfFurnitureDTO {

  @JsonView(Views.Public.class)
  private int id;
  @JsonView(Views.Public.class)
  private String label;

  @Override
  public int getId() {
    return this.id;
  }

  @Override
  public void setId(int id) {
    this.id = id;
  }

  @Override
  public String getLabel() {
    return this.label;
  }

  @Override
  public void setLabel(String label) {
    this.label = label;
  }


}
