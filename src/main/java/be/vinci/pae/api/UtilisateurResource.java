package be.vinci.pae.api;

// import be.vinci.pae.api.filters.Authorize;
// import be.vinci.pae.api.utils.Json;
import be.vinci.pae.domain.UtilisateurFactory;
import be.vinci.pae.domain.UtilisateurUCC;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.ws.rs.Path;

@Singleton
@Path("/utilisateurs")
public class UtilisateurResource {

  @Inject
  private UtilisateurUCC utilisateurUCC;

  @Inject
  private UtilisateurFactory utilisateurFactory;

}
