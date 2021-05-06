package be.vinci.pae.utils;

import java.util.logging.Logger;

import org.glassfish.hk2.utilities.binding.AbstractBinder;

import be.vinci.pae.domain.address.AddressFactory;
import be.vinci.pae.domain.address.AddressFactoryImpl;
import be.vinci.pae.domain.edition.EditionFactory;
import be.vinci.pae.domain.edition.EditionFactoryImpl;
import be.vinci.pae.domain.furniture.FurnitureFactory;
import be.vinci.pae.domain.furniture.FurnitureFactoryImpl;
import be.vinci.pae.domain.furniture.FurnitureUCC;
import be.vinci.pae.domain.furniture.FurnitureUCCImpl;
import be.vinci.pae.domain.furniture.OptionFactory;
import be.vinci.pae.domain.furniture.OptionFactoryImpl;
import be.vinci.pae.domain.furniture.TypeOfFurnitureFactory;
import be.vinci.pae.domain.furniture.TypeOfFurnitureFactoryImpl;
import be.vinci.pae.domain.sale.SaleFactory;
import be.vinci.pae.domain.sale.SaleFactoryImpl;
import be.vinci.pae.domain.sale.SaleUCC;
import be.vinci.pae.domain.sale.SaleUCCImpl;
import be.vinci.pae.domain.user.UserFactory;
import be.vinci.pae.domain.user.UserFactoryImpl;
import be.vinci.pae.domain.user.UserUCC;
import be.vinci.pae.domain.user.UserUCCImpl;
import be.vinci.pae.domain.visit.PhotoFactory;
import be.vinci.pae.domain.visit.PhotoFactoryImpl;
import be.vinci.pae.domain.visit.VisitFactory;
import be.vinci.pae.domain.visit.VisitFactoryImpl;
import be.vinci.pae.domain.visit.VisitUCC;
import be.vinci.pae.domain.visit.VisitUCCImpl;
import be.vinci.pae.services.dal.DalBackendServices;
import be.vinci.pae.services.dal.DalServices;
import be.vinci.pae.services.dal.DalServicesImpl;
import be.vinci.pae.services.dao.AddressDAO;
import be.vinci.pae.services.dao.AddressDAOImpl;
import be.vinci.pae.services.dao.FurnitureDAO;
import be.vinci.pae.services.dao.FurnitureDAOImpl;
import be.vinci.pae.services.dao.SaleDAO;
import be.vinci.pae.services.dao.SaleDAOImpl;
import be.vinci.pae.services.dao.UserDAO;
import be.vinci.pae.services.dao.UserDAOImpl;
import be.vinci.pae.services.dao.VisitDAO;
import be.vinci.pae.services.dao.VisitDAOImpl;
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
