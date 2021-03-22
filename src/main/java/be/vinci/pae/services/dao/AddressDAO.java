package be.vinci.pae.services.dao;

import be.vinci.pae.domain.address.Address;

public interface AddressDAO {

  int addAddress(Address address);

  Address getAddress(int id);
}
