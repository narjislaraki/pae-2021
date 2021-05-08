package be.vinci.pae.services.dao.interfaces;

import be.vinci.pae.domain.interfaces.AddressDTO;

public interface AddressDAO {

  /**
   * Add an address to the database.
   * 
   * @param address the address to add
   * @return the id of the address added or 0 if an error has arisen
   */
  int addAddress(AddressDTO address);

  /**
   * Get the address whose id is passed as a parameter.
   * 
   * @param id the address id
   * @return the address or null if there is no address with this id.
   */
  AddressDTO getAddress(int id);
}
