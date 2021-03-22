package be.vinci.pae.utils;

import org.glassfish.hk2.utilities.binding.AbstractBinder;
import be.vinci.pae.domain.address.AddressFactory;
import be.vinci.pae.domain.address.AddressFactoryImpl;
import be.vinci.pae.domain.admin.AdminUCC;
import be.vinci.pae.domain.admin.AdminUCCImpl;
import be.vinci.pae.domain.user.UserFactory;
import be.vinci.pae.domain.user.UserFactoryImpl;
import be.vinci.pae.domain.user.UserUCC;
import be.vinci.pae.domain.user.UserUCCImpl;
import be.vinci.pae.services.dal.DalServices;
import be.vinci.pae.services.dal.DalServicesImpl;
import be.vinci.pae.services.dao.AddressDAO;
import be.vinci.pae.services.dao.AdminDAO;
import be.vinci.pae.services.dao.UserDAO;
import jakarta.inject.Singleton;
import jakarta.ws.rs.ext.Provider;

@Provider
public class ApplicationBinder extends AbstractBinder {

  @Override
  protected void configure() {
    Class<?> userDAO = null;
    Class<?> addressDAO = null;
    Class<?> adminDAO = null;
    try {
      userDAO = Class.forName(Config.getStringProperty("be.vinci.pae.services.UserDAO"));
      addressDAO = Class.forName(Config.getStringProperty("be.vinci.pae.services.AddressDAO"));
      adminDAO = Class.forName(Config.getStringProperty("be.vinci.pae.services.AdminDAO"));
    } catch (ClassNotFoundException e) {
      // TODO Auto-generated catch block
      System.out.println(e);
      e.printStackTrace();
    }
    bind(UserFactoryImpl.class).to(UserFactory.class).in(Singleton.class);
    bind(userDAO).to(UserDAO.class).in(Singleton.class);
    bind(UserUCCImpl.class).to(UserUCC.class).in(Singleton.class);
    bind(AddressFactoryImpl.class).to(AddressFactory.class).in(Singleton.class);
    bind(addressDAO).to(AddressDAO.class).in(Singleton.class);
    bind(DalServicesImpl.class).to(DalServices.class).in(Singleton.class);
    bind(adminDAO).to(AdminDAO.class).in(Singleton.class);
    bind(AdminUCCImpl.class).to(AdminUCC.class).in(Singleton.class);
  }
}
