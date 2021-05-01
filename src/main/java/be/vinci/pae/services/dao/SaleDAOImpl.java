package be.vinci.pae.services.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import be.vinci.pae.domain.sale.SaleDTO;
import be.vinci.pae.domain.sale.SaleFactory;
import be.vinci.pae.exceptions.FatalException;
import be.vinci.pae.services.dal.DalBackendServices;
import jakarta.inject.Inject;

public class SaleDAOImpl implements SaleDAO {

  @Inject
  private DalBackendServices dalBackendService;

  @Inject
  private SaleFactory saleFactory;

  PreparedStatement ps;

  @Override
  public void addSale(SaleDTO sale) {
    try {
      String sql = "INSERT INTO pae.sales VALUES(DEFAULT, ?, ?, ?, ?)";
      ps = dalBackendService.getPreparedStatement(sql);
      ps.setDouble(1, sale.getSellingPrice());
      ps.setInt(2, sale.getIdFurniture());
      if (sale.getIdBuyer() == 0) {
        ps.setObject(3, null);
      } else {
        ps.setInt(3, sale.getIdBuyer());
      }
      Timestamp sellingDate = Timestamp.valueOf(sale.getDateOfSale());
      ps.setTimestamp(4, sellingDate);
      ps.execute();
    } catch (SQLException e) {
      throw new FatalException(e);
    }
  }

  @Override
  public List<SaleDTO> getSalesList() {
    List<SaleDTO> list = new ArrayList<SaleDTO>();
    try {
      String sql = "SELECT s.id_sales, s.selling_price, s.id_furniture, s.id_buyer, s.date_of_sale"
          + " FROM pae.sales ";

      ps = dalBackendService.getPreparedStatement(sql);
      ResultSet rs = ps.executeQuery();
      SaleDTO sale = null;
      while (rs.next()) {
        sale = setSale(rs, sale);
        list.add(sale);
      }
    } catch (SQLException e) {
      throw new FatalException(e);
    }
    return list;
  }

  private SaleDTO setSale(ResultSet rs, SaleDTO sale) {
    try {
      sale = saleFactory.getSaleDTO();
      sale.setId(rs.getInt(1));
      sale.setSellingPrice(rs.getDouble(2));
      sale.setIdFurniture(rs.getInt(3));
      sale.setIdBuyer(rs.getInt(4));
      sale.setDateOfSale(rs.getTimestamp(5).toLocalDateTime());
    } catch (SQLException e) {
      throw new FatalException(e);
    }
    return sale;
  }
}
