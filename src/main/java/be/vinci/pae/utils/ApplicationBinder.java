package be.vinci.pae.utils;

import java.util.logging.Logger;

import org.glassfish.hk2.utilities.binding.AbstractBinder;

import be.vinci.pae.domain.address.AddressFactory;
import be.vinci.pae.domain.address.AddressFactoryImpl;
import be.vinci.pae.domain.user.UserFactory;
import be.vinci.pae.domain.user.UserFactoryImpl;
import be.vinci.pae.domain.user.UserUCC;
import be.vinci.pae.domain.user.UserUCCImpl;
import be.vinci.pae.services.dal.DalServices;
import be.vinci.pae.services.dal.DalServicesImpl;
import be.vinci.pae.services.dao.AddressDAO;
import be.vinci.pae.services.dao.AddressDAOImpl;
import be.vinci.pae.services.dao.UserDAO;
import be.vinci.pae.services.dao.UserDAOImpl;
import jakarta.inject.Singleton;
import jakarta.ws.rs.ext.Provider;

@Provider
public class ApplicationBinder extends AbstractBinder {

  @Override
  protected void configure() {
    bind(UserFactoryImpl.class).to(UserFactory.class).in(Singleton.class);
    bind(UserDAOImpl.class).to(UserDAO.class).in(Singleton.class);
    bind(UserUCCImpl.class).to(UserUCC.class).in(Singleton.class);
    bind(AddressFactoryImpl.class).to(AddressFactory.class).in(Singleton.class);
    bind(AddressDAOImpl.class).to(AddressDAO.class).in(Singleton.class);
    bind(DalServicesImpl.class).to(DalServices.class).in(Singleton.class);
    bind(APILogger.getLogger()).to(Logger.class);
  }
}
