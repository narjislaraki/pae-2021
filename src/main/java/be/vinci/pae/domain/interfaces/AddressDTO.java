package be.vinci.pae.domain.interfaces;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import be.vinci.pae.domain.AddressImpl;

@JsonDeserialize(as = AddressImpl.class)
public interface AddressDTO {

  int getId();

  void setId(int id);

  String getUnitNumber();

  void setUnitNumber(String unitNumber);

  String getStreet();

  void setStreet(String street);

  String getBuildingNumber();

  void setBuildingNumber(String buildingNumber);

  String getCity();

  void setCity(String city);

  String getPostCode();

  void setPostCode(String postCode);

  String getCountry();

  void setCountry(String country);

}
