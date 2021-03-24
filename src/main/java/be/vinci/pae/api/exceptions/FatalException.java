package be.vinci.pae.api.exceptions;

import java.util.logging.Logger;

import be.vinci.pae.utils.APILogger;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

public class FatalException extends WebApplicationException {

  private static final long serialVersionUID = 14366112877881039L;
  private static Logger logger = APILogger.getLogger();

  /**
   * Constructor with a Throwable original cause wrapped as parameter.
   * 
   * @param cause the original Throwable element
   */
  public FatalException(Throwable cause) {
    super(cause, Response.status(Status.INTERNAL_SERVER_ERROR).build());
    logger.warning(cause.getMessage());
  }

  /**
   * Constructor with a Throwable original cause wrapped as parameter and a message.
   * 
   * @param message the message
   * @param cause the original Throwable element
   */
  public FatalException(String message, Throwable cause) {
    super(cause,
        Response.status(Status.INTERNAL_SERVER_ERROR).entity(message).type("text/plain").build());
    logger.warning(message + "\n" + cause.getMessage());
  }

}
