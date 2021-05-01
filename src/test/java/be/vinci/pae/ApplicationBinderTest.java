package be.vinci.pae;

import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.mockito.Mockito;

import be.vinci.pae.services.dal.DalServices;
import be.vinci.pae.services.dao.AddressDAO;
import be.vinci.pae.services.dao.FurnitureDAO;
import be.vinci.pae.services.dao.SaleDAO;
import be.vinci.pae.services.dao.UserDAO;
import be.vinci.pae.services.dao.VisitDAO;
import jakarta.ws.rs.ext.Provider;

@Provider
public class ApplicationBinderTest extends AbstractBinder {

  @Override
  protected void configure() {
    bind(Mockito.mock(UserDAO.class)).to(UserDAO.class).ranked(2);
    bind(Mockito.mock(AddressDAO.class)).to(AddressDAO.class).ranked(2);
    bind(Mockito.mock(VisitDAO.class)).to(VisitDAO.class).ranked(2);
    bind(Mockito.mock(FurnitureDAO.class)).to(FurnitureDAO.class).ranked(2);
    bind(Mockito.mock(SaleDAO.class)).to(SaleDAO.class).ranked(2);
    bind(Mockito.mock(DalServices.class)).to(DalServices.class).ranked(2);
  }
}
