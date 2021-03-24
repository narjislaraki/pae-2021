package be.vinci.pae.api.exceptions;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.logging.Logger;

import be.vinci.pae.utils.APILogger;
import be.vinci.pae.utils.Config;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class ExceptionHandler implements ExceptionMapper<Exception> {

  private Logger logger = APILogger.getLogger();


  @Override
  public Response toResponse(Exception exception) {
    if (exception instanceof BusinessException == false) {
      // from stacktrace to String.
      StringWriter sw = new StringWriter();
      PrintWriter pw = new PrintWriter(sw);
      exception.printStackTrace(pw);
      logger.warning(exception.getClass().getCanonicalName() + "\n" + sw.toString());
    }

    if (Config.getBoolProperty("SendStackTraceToClient")) {
      return Response.status(getStatusCode(exception)).entity(getEntity(exception)).build();
    }
    if (exception instanceof FatalException) {
      Throwable cause = exception.getCause();
      if (cause == null) {
        return Response.status(getStatusCode(exception)).entity(exception.getMessage()).build();
      }
      return Response.status(getStatusCode(exception)).entity(cause.getMessage()).build();
    }
    return Response.status(getStatusCode(exception))
        .entity(exception.getMessage() == null || exception.getMessage().isEmpty()
            ? "Oh no, something wrong happened! Don't worry, we're on it!"
            : exception.getMessage())
        .build();
  }

  /*
   * Get appropriate HTTP status code for an exception.
   */
  private int getStatusCode(Throwable exception) {
    if (exception instanceof WebApplicationException) {
      return ((WebApplicationException) exception).getResponse().getStatus();
    } else if (exception instanceof UnauthorizedException) {
      return Response.Status.UNAUTHORIZED.getStatusCode();
    } // Insert new exception rules here when extend BusinessException
    else if (exception instanceof BusinessException) {
      return Response.Status.PRECONDITION_FAILED.getStatusCode();
    }
    return Response.Status.INTERNAL_SERVER_ERROR.getStatusCode();
  }

  /*
   * Get response body for an exception.
   */
  private Object getEntity(Throwable exception) {
    StringWriter errorMsg = new StringWriter();
    exception.printStackTrace(new PrintWriter(errorMsg));
    return errorMsg.toString();
  }

}
