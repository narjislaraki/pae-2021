package be.vinci.pae.api.exceptions;

public abstract class BusinessException extends RuntimeException {

  private static final long serialVersionUID = -5107229850612287828L;
  private String message;

  public String getMessage() {
    return message;
  }
}
