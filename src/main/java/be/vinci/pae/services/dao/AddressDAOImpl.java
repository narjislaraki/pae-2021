package be.vinci.pae.services.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import be.vinci.pae.domain.addresses.Address;
import be.vinci.pae.services.dal.DalServices;
import jakarta.inject.Inject;

public class AddressDAOImpl implements AddressDAO {

  @Inject
  private DalServices dalServices;


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
}
