package be.vinci.pae.services.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import be.vinci.pae.domain.interfaces.AddressDTO;
import be.vinci.pae.exceptions.FatalException;
import be.vinci.pae.factories.interfaces.AddressFactory;
import be.vinci.pae.services.dal.DalBackendServices;
import be.vinci.pae.services.dao.interfaces.AddressDAO;
import jakarta.inject.Inject;

public class AddressDAOImpl implements AddressDAO {

  @Inject
  private DalBackendServices dalBackendServices;

  @Inject
  private AddressFactory addressFactory;

  PreparedStatement ps;


  @Override
  public int addAddress(AddressDTO address) {
    int key = 0;
    try {
      String sql = "INSERT INTO pae.addresses VALUES(default, ?, ?, ?, ?, ?, ?);";
      PreparedStatement ps = dalBackendServices.getPreparedStatementWithGeneratedReturn(sql);
      ps.setString(1, address.getStreet());
      ps.setString(2, address.getBuildingNumber());
      if (address.getUnitNumber() == null || address.getUnitNumber().isEmpty()
          || address.getUnitNumber().equals("")) {
        ps.setObject(3, null);
      } else {
        ps.setString(3, address.getUnitNumber());
      }
      ps.setString(4, address.getCity());
      ps.setString(5, address.getPostCode());
      ps.setString(6, address.getCountry());
      ps.execute();
      ResultSet rs = ps.getGeneratedKeys();
      if (rs.next()) {
        key = rs.getInt(1);
      }
    } catch (SQLException e) {
      throw new FatalException(e);
    }
    return key;

  }



  @Override
  public AddressDTO getAddress(int id) {
    AddressDTO address = null;
    try {
      ps = dalBackendServices
          .getPreparedStatement("SELECT a.id_address, a.street, a.building_number, a.unit_number, "
              + "a.city, a.postcode, a.country " + "FROM pae.addresses a WHERE a.id_address = ?;");

      ps.setInt(1, id);

      ResultSet rs = ps.executeQuery();

      while (rs.next()) {
        address = addressFactory.getAddress();
        address.setId(rs.getInt(1));
        address.setStreet(rs.getString(2));
        address.setBuildingNumber(rs.getString(3) == null ? "0" : rs.getString(3));
        address.setUnitNumber(rs.getString(4));
        address.setCity(rs.getString(5));
        address.setPostCode(rs.getString(6));
        address.setCountry(rs.getString(7));

      }
    } catch (SQLException e) {
      throw new FatalException(e);
    }
    return address;
  }
}
