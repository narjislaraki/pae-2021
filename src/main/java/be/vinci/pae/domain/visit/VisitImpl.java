package be.vinci.pae.domain.visit;

import java.time.LocalDateTime;
import java.util.ArrayList;
import com.fasterxml.jackson.annotation.JsonView;
import be.vinci.pae.domain.address.Address;
import be.vinci.pae.domain.furniture.FurnitureDTO;
import be.vinci.pae.domain.user.User;
import be.vinci.pae.views.Views;

public class VisitImpl implements Visit {

  @JsonView(Views.Public.class)
  private int idRequest;
  @JsonView(Views.Public.class)
  private String timeSlot;
  @JsonView(Views.Public.class)
  private int warehouseAddressId;
  @JsonView(Views.Public.class)
  private Address warehouseAddress;
  @JsonView(Views.Public.class)
  private VisitCondition visitCondition;
  @JsonView(Views.Public.class)
  private int idClient;
  @JsonView(Views.Public.class)
  private User client;
  @JsonView(Views.Public.class)
  private String explanatoryNote;
  @JsonView(Views.Public.class)
  private LocalDateTime scheduledDateTime;
  @JsonView(Views.Public.class)
  private ArrayList<FurnitureDTO> furnitureList;

  @Override
  public int getIdRequest() {
    return this.idRequest;
  }

  @Override
  public void setIdRequest(int idRequest) {
    this.idRequest = idRequest;
  }

  @Override
  public String getTimeSlot() {
    return this.timeSlot;
  }

  @Override
  public void setTimeSlot(String timeSlot) {
    this.timeSlot = timeSlot;
  }

  @Override
  public Address getWarehouseAddress() {
    return this.warehouseAddress;
  }

  @Override
  public void setWarehouseAddress(Address address) {
    this.warehouseAddress = address;
  }

  @Override
  public VisitCondition getVisitCondition() {
    return this.visitCondition;
  }

  @Override
  public String toString() {
    return "VisitImpl [idRequest=" + idRequest + ", timeSlot=" + timeSlot + ", warehouseAddressId="
        + warehouseAddressId + ", warehouseAddress=" + warehouseAddress + ", visitCondition="
        + visitCondition + ", idClient=" + idClient + ", client=" + client + ", explanatoryNote="
        + explanatoryNote + ", scheduledDateTime=" + scheduledDateTime + ", furnitureList="
        + furnitureList + "]";
  }

  @Override
  public void setVisitCondition(String visitCondition) {
    switch (visitCondition.toLowerCase()) {

      case "en attente":
        this.visitCondition = VisitCondition.EN_ATTENTE;
        break;
      case "acceptee":
        this.visitCondition = VisitCondition.ACCEPTEE;
        break;
      case "annulee":
        this.visitCondition = VisitCondition.ANNULEE;
        break;
      default:
        throw new IllegalArgumentException();
    }
  }

  @Override
  public int getIdClient() {
    return this.idClient;
  }

  @Override
  public void setIdClient(int idClient) {
    this.idClient = idClient;
  }

  @Override
  public User getClient() {
    return this.client;
  }

  @Override
  public void setClient(User client) {
    this.client = client;
  }

  @Override
  public String getExplanatoryNote() {
    return this.explanatoryNote;
  }

  @Override
  public void setExplanatoryNote(String explanatoryNote) {
    this.explanatoryNote = explanatoryNote;
  }

  @Override
  public LocalDateTime getScheduledDateTime() {
    return this.scheduledDateTime;
  }

  @Override
  public void setScheduledDateTime(LocalDateTime scheduledDateTime) {
    this.scheduledDateTime = scheduledDateTime;
  }

  @Override
  public int getWarehouseAddressId() {
    return this.warehouseAddressId;
  }

  @Override
  public void setWarehouseAddressId(int id) {
    this.warehouseAddressId = id;
  }

  @Override
  public ArrayList<FurnitureDTO> getFurnitureList() {
    return this.furnitureList;
  }

  @Override
  public void setFurnitureList(ArrayList<FurnitureDTO> furnitureList) {
    this.furnitureList = furnitureList;
  }

}
