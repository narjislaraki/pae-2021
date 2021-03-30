package be.vinci.pae.domain.furniture;

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

  public int getId();

  public void setId(int id);

  public LocalDateTime getDate();

  public void setDate(LocalDateTime date);

  public int getOptionTerm();

  public void setOptionTerm(int optionTerm);

  public String getCancellationReason();

  public void setCancellationReason(String cancellationReason);

  public State getCondition();

  public void setCondition(String condition);

  public int getIdUser();

  public void setIdUser(int idUser);

  public int getIdFurniture();

  public void setIdFurniture(int idFurniture);
}
