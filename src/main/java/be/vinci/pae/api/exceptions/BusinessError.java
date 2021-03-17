package be.vinci.pae.api.exceptions;

public class BusinessError extends Error {

  private static final long serialVersionUID = -5107229850612287828L;
  private String message;

  public BusinessError(String message) {
    this.message = message;
  }

  public String getMessage() {
    return message;
  }


}
