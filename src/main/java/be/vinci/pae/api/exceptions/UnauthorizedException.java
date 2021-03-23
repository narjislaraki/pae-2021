package be.vinci.pae.api.exceptions;

public class UnauthorizedException extends BusinessException {

  private static final long serialVersionUID = -4333601375181117263L;
  private String message;

  public UnauthorizedException(String message) {
    super(message);
  }

  public String getMessage() {
    return message;
  }


}
