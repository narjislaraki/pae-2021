package be.vinci.pae.services.dao;

import be.vinci.pae.domain.furniture.Furniture;

public interface FurnitureDAO {

  void setCondition(Furniture furniture, String condition);
}
