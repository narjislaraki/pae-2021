package be.vinci.pae.domain.interfaces;

import java.time.LocalDateTime;


public interface OptionDTO {

  enum State {
    EN_COURS("en cours"), ANNULEE("annulée");

    private String state;

    State(String state) {
      this.state = state;
    }

    public String toString() {
      return this.state;
    }
  }

  int getId();

  void setId(int id);

  LocalDateTime getDate();

  void setDate(LocalDateTime date);

  int getOptionTerm();

  void setOptionTerm(int optionTerm);

  String getCancellationReason();

  void setCancellationReason(String cancellationReason);

  State getCondition();

  void setCondition(String condition);

  int getIdUser();

  void setIdUser(int idUser);

  int getIdFurniture();

  void setIdFurniture(int idFurniture);
}
