package be.vinci.pae.domain.visit;

import java.time.LocalDateTime;
import be.vinci.pae.domain.address.Address;
import be.vinci.pae.domain.user.User;

public interface VisitDTO {

  enum VisitCondition {
    EN_ATTENTE("en attente"), ACCEPTEE("acceptée"), ANNULEE("annulée");

    private String visitCondition;

    VisitCondition(String visitCondition) {
      this.visitCondition = visitCondition;
    }

    public String toString() {
      return this.visitCondition;
    }
  }

  int getIdRequest();

  void setIdRequest(int idRequest);

  String getTimeSlot();

  void setTimeSlot(String timeSlot);

  int getWarehouseAddressId();

  void setWarehouseAddressId(int id);

  Address getWarehouseAddress();

  void setWarehouseAddress(Address address);

  VisitCondition getVisitCondition();

  void setVisitCondition(String visitCondition);

  User getClient();

  void setClient(User client);

  int getIdClient();

  void setIdClient(int idClient);

  String getExplanatoryNote();

  void setExplanatoryNote(String explanatoryNote);

  LocalDateTime getScheduledDateTime();

  void setScheduledDateTime(LocalDateTime scheduledDateTime);

}
