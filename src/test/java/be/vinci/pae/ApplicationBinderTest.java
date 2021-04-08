package be.vinci.pae;

import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.mockito.Mockito;

import be.vinci.pae.services.dal.DalServices;
import be.vinci.pae.services.dao.AddressDAO;
import be.vinci.pae.services.dao.UserDAO;
import jakarta.ws.rs.ext.Provider;

@Provider
public class ApplicationBinderTest extends AbstractBinder {

  @Override
  protected void configure() {
    bind(Mockito.mock(UserDAO.class)).to(UserDAO.class).ranked(2);
    bind(Mockito.mock(AddressDAO.class)).to(AddressDAO.class).ranked(2);
    bind(Mockito.mock(DalServices.class)).to(DalServices.class).ranked(2);
  }
}
