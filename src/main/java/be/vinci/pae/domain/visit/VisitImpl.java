package be.vinci.pae.domain.visit;

import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonView;
import be.vinci.pae.domain.address.Address;
import be.vinci.pae.domain.user.User;
import be.vinci.pae.views.Views;

public class VisitImpl implements Visit {

  @JsonView(Views.Public.class)
  private int idReqeust;
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

  @Override
  public int getIdRequest() {
    return this.idReqeust;
  }

  @Override
  public void setIdRequest(int idRequest) {
    this.idReqeust = idRequest;
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

}
