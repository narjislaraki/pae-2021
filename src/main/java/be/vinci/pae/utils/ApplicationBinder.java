package be.vinci.pae.utils;

import java.util.logging.Logger;

import org.glassfish.hk2.utilities.binding.AbstractBinder;

import be.vinci.pae.factories.AddressFactoryImpl;
import be.vinci.pae.factories.EditionFactoryImpl;
import be.vinci.pae.factories.FurnitureFactoryImpl;
import be.vinci.pae.factories.OptionFactoryImpl;
import be.vinci.pae.factories.PhotoFactoryImpl;
import be.vinci.pae.factories.SaleFactoryImpl;
import be.vinci.pae.factories.TypeOfFurnitureFactoryImpl;
import be.vinci.pae.factories.UserFactoryImpl;
import be.vinci.pae.factories.VisitFactoryImpl;
import be.vinci.pae.factories.interfaces.AddressFactory;
import be.vinci.pae.factories.interfaces.EditionFactory;
import be.vinci.pae.factories.interfaces.FurnitureFactory;
import be.vinci.pae.factories.interfaces.OptionFactory;
import be.vinci.pae.factories.interfaces.PhotoFactory;
import be.vinci.pae.factories.interfaces.SaleFactory;
import be.vinci.pae.factories.interfaces.TypeOfFurnitureFactory;
import be.vinci.pae.factories.interfaces.UserFactory;
import be.vinci.pae.factories.interfaces.VisitFactory;
import be.vinci.pae.services.dal.DalBackendServices;
import be.vinci.pae.services.dal.DalServices;
import be.vinci.pae.services.dal.DalServicesImpl;
import be.vinci.pae.services.dao.AddressDAOImpl;
import be.vinci.pae.services.dao.FurnitureDAOImpl;
import be.vinci.pae.services.dao.SaleDAOImpl;
import be.vinci.pae.services.dao.UserDAOImpl;
import be.vinci.pae.services.dao.VisitDAOImpl;
import be.vinci.pae.services.dao.interfaces.AddressDAO;
import be.vinci.pae.services.dao.interfaces.FurnitureDAO;
import be.vinci.pae.services.dao.interfaces.SaleDAO;
import be.vinci.pae.services.dao.interfaces.UserDAO;
import be.vinci.pae.services.dao.interfaces.VisitDAO;
import be.vinci.pae.ucc.FurnitureUCCImpl;
import be.vinci.pae.ucc.SaleUCCImpl;
import be.vinci.pae.ucc.UserUCCImpl;
import be.vinci.pae.ucc.VisitUCCImpl;
import be.vinci.pae.ucc.interfaces.FurnitureUCC;
import be.vinci.pae.ucc.interfaces.SaleUCC;
import be.vinci.pae.ucc.interfaces.UserUCC;
import be.vinci.pae.ucc.interfaces.VisitUCC;
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
    bind(DalServicesImpl.class).to(DalServices.class).to(DalBackendServices.class)
        .in(Singleton.class);
    bind(FurnitureDAOImpl.class).to(FurnitureDAO.class).in(Singleton.class);
    bind(FurnitureUCCImpl.class).to(FurnitureUCC.class).in(Singleton.class);
    bind(FurnitureFactoryImpl.class).to(FurnitureFactory.class).in(Singleton.class);
    bind(OptionFactoryImpl.class).to(OptionFactory.class).in(Singleton.class);
    bind(TypeOfFurnitureFactoryImpl.class).to(TypeOfFurnitureFactory.class).in(Singleton.class);
    bind(PhotoFactoryImpl.class).to(PhotoFactory.class).in(Singleton.class);
    bind(VisitFactoryImpl.class).to(VisitFactory.class).in(Singleton.class);
    bind(VisitDAOImpl.class).to(VisitDAO.class).in(Singleton.class);
    bind(VisitUCCImpl.class).to(VisitUCC.class).in(Singleton.class);
    bind(SaleDAOImpl.class).to(SaleDAO.class).in(Singleton.class);
    bind(SaleFactoryImpl.class).to(SaleFactory.class).in(Singleton.class);
    bind(EditionFactoryImpl.class).to(EditionFactory.class).in(Singleton.class);
    bind(SaleUCCImpl.class).to(SaleUCC.class).in(Singleton.class);
    bind(APILogger.getLogger()).to(Logger.class);
  }
}
