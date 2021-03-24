package be.vinci.pae.api.exceptions;

public class UnauthorizedException extends BusinessException {

  private static final long serialVersionUID = -4333601375181117263L;

  public UnauthorizedException(String message) {
    super(message);
  }

}
