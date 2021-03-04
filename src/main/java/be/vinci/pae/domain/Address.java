package be.vinci.pae.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(as = AddressImpl.class)
public interface Address {

  int getId();

  void setId(int id);

  public int unitNumber();

  public void setUnitNumber(int unitNumber);

  public String getStreet();

  public void setStreet(String street);

  public String getBuildingNumber();

  public void setBuildingNumber(String buildingNumber);

  public String getCity();

  public void setCity(String city);

  public String getPostCode();

  public void setPostCode(String postCode);

  public String getCountry();

  public void setCountry(String country);
}
