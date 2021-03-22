package be.vinci.pae.services.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import be.vinci.pae.api.exceptions.FatalException;
import be.vinci.pae.domain.address.Address;
import be.vinci.pae.domain.address.AddressFactory;
import be.vinci.pae.services.dal.DalServices;
import jakarta.inject.Inject;

public class AddressDAOImpl implements AddressDAO {

  @Inject
  private DalServices dalServices;

  @Inject
  private AddressFactory addressFactory;

  PreparedStatement ps;


  @Override
  public int addAddress(Address address) {
    int key = 0;
    try {
      String sql = "INSERT INTO pae.addresses VALUES(default, ?, ?, ?, ?, ?, ?);";
      PreparedStatement ps = dalServices.getPreparedStatementWithGeneratedReturn(sql);
      ps.setString(1, address.getStreet());
      ps.setString(2, address.getBuildingNumber());
      if (address.getUnitNumber() != 0) {
        ps.setInt(3, address.getUnitNumber());
      } else {
        ps.setObject(3, null);
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
      e.printStackTrace();
    }
    return key;

  }



  @Override
  public Address getAddress(int id) {
    Address address = null;
    try {
      ps = dalServices
          .getPreparedStatement("SELECT a.id_address, a.street, a.building_number, a.unit_number, "
              + "a.city, a.postcode, a.country " + "FROM pae.addresses a WHERE a.id_address = ?;");

      ps.setInt(1, id);

      ResultSet rs = ps.executeQuery();

      while (rs.next()) {
        address = addressFactory.getAddress();
        address.setId(rs.getInt(1));
        address.setStreet(rs.getString(2));
        address.setBuildingNumber(rs.getString(3));
        address.setUnitNumber(rs.getInt(4));
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
