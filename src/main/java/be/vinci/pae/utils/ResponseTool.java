package be.vinci.pae.utils;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

public class ResponseTool {
  /**
   * Quick way to construct HTTP Response with status and entity.
   * 
   * @param status the status
   * @param obj the entity
   * @return a Response containing the status and the message
   */
  public static Response responseWithStatus(Status status, Object obj) {
    return Response.status(status).entity(obj).type(MediaType.TEXT_PLAIN).build();
  }

  /**
   * Quick way to construct HTTP ok Response with entity.
   * 
   * @param obj the entity
   * @return
   */
  public static Response responseOkWithEntity(Object obj) {
    return Response.ok(obj, MediaType.APPLICATION_JSON).build();
  }
}
