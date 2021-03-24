package be.vinci.pae.api.filters;

import be.vinci.pae.api.exceptions.UnauthorizedException;
import be.vinci.pae.domain.user.UserDTO;
import jakarta.inject.Singleton;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.ext.Provider;

@Singleton
@Provider
@AdminAuthorize
public class AdminAuthorizationRequestFilter extends AuthorizationRequestFilter {

  @Override
  public void filter(ContainerRequestContext requestContext) {
    super.filter(requestContext);
    UserDTO currentUser = (UserDTO) requestContext.getProperty("user");

    if (!currentUser.getRole().getString().toLowerCase().equals("admin")) {
      throw new UnauthorizedException("Admin only");
    }
  }
}
