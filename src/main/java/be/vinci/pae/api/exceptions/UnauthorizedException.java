package be.vinci.pae.api.exceptions;

public class UnauthorizedException extends BusinessException {

  private static final long serialVersionUID = -5107229850612287828L;
  private String message;

  public UnauthorizedException(String message) {
    this.message = message;
  }

  public String getMessage() {
    return message;
  }


}
