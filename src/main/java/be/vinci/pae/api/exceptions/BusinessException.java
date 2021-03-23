package be.vinci.pae.api.exceptions;

public class BusinessException extends RuntimeException {

  private static final long serialVersionUID = -5107229850612287828L;
  private String message;

  public BusinessException(String message) {
    this.message = message;
  }

  public BusinessException() {

  }

  public String getMessage() {
    return message;
  }
}
