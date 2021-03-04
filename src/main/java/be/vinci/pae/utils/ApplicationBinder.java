package be.vinci.pae.utils;

import be.vinci.pae.domain.UtilisateurFactory;
import be.vinci.pae.domain.UtilisateurFactoryImpl;
import be.vinci.pae.services.UtilisateurDAO;
import be.vinci.pae.services.UtilisateurDAOImpl;

import org.glassfish.hk2.utilities.binding.AbstractBinder;

import jakarta.inject.Singleton;
import jakarta.ws.rs.ext.Provider;

@Provider
public class ApplicationBinder extends AbstractBinder {

  @Override
  protected void configure() {
    bind(UtilisateurFactoryImpl.class).to(UtilisateurFactory.class).in(Singleton.class);
    bind(UtilisateurDAOImpl.class).to(UtilisateurDAO.class).in(Singleton.class);
  }
}
