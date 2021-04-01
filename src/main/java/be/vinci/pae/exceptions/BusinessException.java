package be.vinci.pae.exceptions;

public class BusinessException extends RuntimeException {

  private static final long serialVersionUID = -5107229850612287828L;

  public BusinessException(String message) {
    super(message);
  }

  public BusinessException() {

  }
}

