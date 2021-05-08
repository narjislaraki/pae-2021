package be.vinci.pae.services.dao;

import be.vinci.pae.domain.interfaces.AddressDTO;

public interface AddressDAO {

  int addAddress(AddressDTO address);

  AddressDTO getAddress(int id);
}
