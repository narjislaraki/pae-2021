package be.vinci.pae.api;

import static be.vinci.pae.utils.ResponseTool.responseOkWithEntity;
import static be.vinci.pae.utils.ResponseTool.responseWithStatus;

import java.util.List;

import org.glassfish.jersey.server.ContainerRequest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import be.vinci.pae.api.filters.AdminAuthorize;
import be.vinci.pae.domain.user.UserDTO;
import be.vinci.pae.domain.user.UserUCC;
import be.vinci.pae.views.Views;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

@Singleton
@Path("/users")
public class UserResource {

	private final ObjectMapper jsonMapper = new ObjectMapper();

	@Inject
	private UserUCC userUCC;

	public UserResource() {
		jsonMapper.findAndRegisterModules();
		jsonMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
	}

	/**
	 * Get a list of unvalidated users.
	 * 
	 * @param request the request
	 * @return a list of unvalidated users wrapped in a Response
	 */
	@GET
	@Path("unvalidatedList")
	@AdminAuthorize
	public Response getListOfUnvalidatedUsers(@Context ContainerRequest request) {
		List<UserDTO> list = userUCC.getUnvalidatedUsers();

		String r = null;
		try {
			r = jsonMapper.writerWithView(Views.Private.class).writeValueAsString(list);
		} catch (JsonProcessingException e) {
			return responseWithStatus(Status.INTERNAL_SERVER_ERROR, "Problem while converting data");
		}
		return responseOkWithEntity(r);
	}

	/**
	 * Get a list of validated users.
	 * 
	 * @param request the request
	 * @return a list of validated users wrapped in a Response
	 */
	@GET
	@Path("validatedList")
	@AdminAuthorize
	public Response getListOfUsers(@Context ContainerRequest request) {
		List<UserDTO> list = userUCC.getValidatedUsers();

		String r = null;
		try {
			r = jsonMapper.writerWithView(Views.Private.class).writeValueAsString(list);
		} catch (JsonProcessingException e) {
			return responseWithStatus(Status.INTERNAL_SERVER_ERROR, "Problem while converting data");
		}
		return responseOkWithEntity(r);
	}

	/**
	 * Validation of a user.
	 * 
	 * @param request the request
	 * @param id      the user's id
	 * @return true if OK
	 */
	@POST
	@Path("{id}/accept")
	@AdminAuthorize
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public boolean acceptUser(@Context ContainerRequest request, @PathParam("id") int id, JsonNode json) {
		String role = json.get("role").asText();
		return userUCC.acceptUser(id, role);
	}

	/**
	 * Deletion of a user.
	 * 
	 * @param request the request
	 * @param id      the id user's id
	 * @return true if OK, false if nKO
	 */
	@DELETE
	@Path("{id}")
	@AdminAuthorize
	@Produces(MediaType.APPLICATION_JSON)
	public boolean refuseUser(@Context ContainerRequest request, @PathParam("id") int id) {
		return userUCC.deleteUser(id);
	}

}
