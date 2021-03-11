package be.vinci.pae.utils;

import org.glassfish.hk2.utilities.binding.AbstractBinder;

import be.vinci.pae.domain.addresses.AddressFactory;
import be.vinci.pae.domain.addresses.AddressFactoryImpl;
import be.vinci.pae.domain.user.UserFactory;
import be.vinci.pae.domain.user.UserFactoryImpl;
import be.vinci.pae.domain.user.UserUCC;
import be.vinci.pae.domain.user.UserUCCImpl;
import be.vinci.pae.services.DAL.DalServices;
import be.vinci.pae.services.DAL.DalServicesImpl;
import be.vinci.pae.services.DAO.UserDAO;
import jakarta.inject.Singleton;
import jakarta.ws.rs.ext.Provider;

@Provider
public class ApplicationBinder extends AbstractBinder {

  @Override
  protected void configure() {
    Class<?> userDAO = null;
    try {
      userDAO = Class.forName(Config.getProperty("be.vinci.pae.services.UserDAO"));
    } catch (ClassNotFoundException e) {
      // TODO Auto-generated catch block
      System.out.println(e);
      e.printStackTrace();
    }
    bind(UserFactoryImpl.class).to(UserFactory.class).in(Singleton.class);
    bind(userDAO).to(UserDAO.class).in(Singleton.class);
    bind(UserUCCImpl.class).to(UserUCC.class).in(Singleton.class);
    bind(AddressFactoryImpl.class).to(AddressFactory.class).in(Singleton.class);
    bind(DalServicesImpl.class).to(DalServices.class).in(Singleton.class);
  }
}
