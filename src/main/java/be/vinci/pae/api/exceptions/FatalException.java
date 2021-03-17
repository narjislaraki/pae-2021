package be.vinci.pae.api.exceptions;

import java.sql.SQLException;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

public class FatalException extends WebApplicationException {

  private static final long serialVersionUID = 14366112877881039L;

  public FatalException(Throwable cause) {
    super(Response.status(getErrorStatus(cause)).build());
  }

  public FatalException(String message, Throwable cause) {
    super(cause, Response.status(getErrorStatus(cause)).entity(message).type("text/plain").build());
  }

  public FatalException(String message, Status status) {
    super(Response.status(status).entity(message).type("text/plain").build());
  }

  private static Status getErrorStatus(Throwable cause) {
    Status status = Status.INTERNAL_SERVER_ERROR;

    if (cause instanceof SQLException) {
      status = Status.SERVICE_UNAVAILABLE;
    }

    return status;
  }
}
