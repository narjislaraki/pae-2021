package be.vinci.pae.domain.address;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(as = AddressImpl.class)
public interface Address {

  int getId();

  void setId(int id);

  int getUnitNumber();

  void setUnitNumber(int unitNumber);

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
