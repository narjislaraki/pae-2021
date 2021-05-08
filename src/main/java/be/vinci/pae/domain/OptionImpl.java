package be.vinci.pae.domain;

import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonView;

import be.vinci.pae.domain.interfaces.OptionDTO;
import be.vinci.pae.exceptions.BusinessException;
import be.vinci.pae.views.Views;

public class OptionImpl implements OptionDTO {

  @JsonView(Views.Public.class)
  private int id;
  @JsonView(Views.Public.class)
  private LocalDateTime date;
  @JsonView(Views.Public.class)
  private int optionTerm;
  @JsonView(Views.Public.class)
  private String cancellationReason;
  @JsonView(Views.Public.class)
  private State condition;
  @JsonView(Views.Public.class)
  private int idUser;
  @JsonView(Views.Public.class)
  private int idFurniture;

  @Override
  public int getId() {
    return this.id;
  }

  @Override
  public void setId(int id) {
    this.id = id;

  }

  @Override
  public LocalDateTime getDate() {
    return this.date;
  }

  @Override
  public void setDate(LocalDateTime date) {
    this.date = date;
  }

  @Override
  public int getOptionTerm() {
    return this.optionTerm;
  }

  @Override
  public void setOptionTerm(int optionTerm) {
    this.optionTerm = optionTerm;
  }

  @Override
  public String getCancellationReason() {
    return this.cancellationReason;
  }

  @Override
  public void setCancellationReason(String cancellationReason) {
    this.cancellationReason = cancellationReason;
  }

  @Override
  public State getCondition() {
    return this.condition;
  }

  @Override
  public void setCondition(String condition) {
    switch (condition) {
      case "en cours":
        this.condition = State.EN_COURS;
        break;
      case "annulée":
        this.condition = State.ANNULEE;
        break;
      default:
        throw new BusinessException("Set state failed");
    }
  }

  @Override
  public int getIdUser() {
    return this.idUser;
  }

  @Override
  public void setIdUser(int idUser) {
    this.idUser = idUser;
  }

  @Override
  public int getIdFurniture() {
    return this.idFurniture;
  }

  @Override
  public void setIdFurniture(int idFurniture) {
    this.idFurniture = idFurniture;
  }


}
