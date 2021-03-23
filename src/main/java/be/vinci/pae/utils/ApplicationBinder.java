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
import be.vinci.pae.services.dao.UtilsDAO;
import jakarta.inject.Singleton;
import jakarta.ws.rs.ext.Provider;

@Provider
public class ApplicationBinder extends AbstractBinder {

  @Override
  protected void configure() {
    Class<?> userDAO = null;
    Class<?> addressDAO = null;
    Class<?> adminDAO = null;
    Class<?> utilsDAO = null;
    try {
      userDAO = Class.forName(Config.getStringProperty("UserDAO"));
      addressDAO = Class.forName(Config.getStringProperty("AddressDAO"));
      adminDAO = Class.forName(Config.getStringProperty("AdminDAO"));
      utilsDAO = Class.forName(Config.getStringProperty("UtilsDAO"));
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
    bind(utilsDAO).to(UtilsDAO.class).in(Singleton.class);

  }
}
