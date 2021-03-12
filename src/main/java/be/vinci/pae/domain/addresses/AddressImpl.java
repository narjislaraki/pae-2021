package be.vinci.pae.domain.addresses;

public class AddressImpl implements Address {

  private int id;
  private int unitNumber;
  private String street;
  private String buildingNumber;
  private String city;
  private String postCode;
  private String country;

  @Override
  public int getId() {
    return id;
  }

  @Override
  public void setId(int id) {
    this.id = id;
  }

  @Override
  public int getUnitNumber() {
    return unitNumber;
  }

  @Override
  public void setUnitNumber(int unitNumber) {
    this.unitNumber = unitNumber;
  }

  @Override
  public String getStreet() {
    return street;
  }

  @Override
  public void setStreet(String street) {
    this.street = street;
  }

  @Override
  public String getBuildingNumber() {
    return buildingNumber;
  }

  @Override
  public void setBuildingNumber(String buildingNumber) {
    this.buildingNumber = buildingNumber;
  }

  @Override
  public String getCity() {
    return city;
  }

  @Override
  public void setCity(String city) {
    this.city = city;
  }

  @Override
  public String getPostCode() {
    return postCode;
  }

  @Override
  public void setPostCode(String postCode) {
    this.postCode = postCode;
  }

  @Override
  public String getCountry() {
    return country;
  }

  @Override
  public void setCountry(String country) {
    this.country = country;
  }


}
