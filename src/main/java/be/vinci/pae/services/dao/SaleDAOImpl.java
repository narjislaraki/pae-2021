package be.vinci.pae.services.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import be.vinci.pae.domain.furniture.FurnitureDTO;
import be.vinci.pae.domain.furniture.FurnitureFactory;
import be.vinci.pae.domain.sale.SaleDTO;
import be.vinci.pae.domain.sale.SaleFactory;
import be.vinci.pae.domain.sale.SaleImpl;
import be.vinci.pae.exceptions.FatalException;
import be.vinci.pae.services.dal.DalBackendServices;
import jakarta.inject.Inject;

public class SaleDAOImpl implements SaleDAO {

  @Inject
  private DalBackendServices dalBackendService;


  @Inject
  private FurnitureFactory furnitureFactory;

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
  public List<SaleDTO> getTransactionsBuyer(int id) {
    List<SaleDTO> list = new ArrayList<SaleDTO>();
    try {
      String sql = "SELECT s.id_sales, s.selling_price, s.id_furniture, s.id_buyer, s.date_of_sale, "
          + "f.id_furniture, f.description, f.purchase_price, "
          + "f.pick_up_date, f.store_deposit, f.deposit_date, "
          + "f.offered_selling_price, f.id_type, f.request_visit, f.seller, f.favorite_photo, " 
          + "p.photo "
          + "FROM pae.sales s, pae.furnitures f, pae.photos p "
          + "WHERE p.id_photo = f.favorite_photo AND s.id_buyer = ? AND f.id_furniture = s.id_furniture;";
      ps = dalBackendService.getPreparedStatement(sql);
      ps.setInt(1, id);
      ResultSet rs = ps.executeQuery();
      SaleDTO sale = null;
      while (rs.next()) {
        SaleDTO saleDTO = setTransaction(rs, sale);
        list.add(saleDTO);
      }
    } catch (SQLException e) {
      throw new FatalException(e);
    }
    return list;
  }

  @Override
  public List<SaleDTO> getTransactionsSeller(int id) {
    List<SaleDTO> list = new ArrayList<SaleDTO>();
    try {
      String sql = "SELECT s.id_sales, s.selling_price, s.id_furniture, s.id_buyer, s.date_of_sale,"
          + " f.id_furniture, f.description, f.purchase_price, "
          + "f.pick_up_date, f.store_deposit, f.deposit_date, "
          + "f.offered_selling_price, f.id_type, " + "f.request_visit, f.seller, f.favorite_photo, "
          + "p.photo "
          + "FROM pae.sales s, pae.furnitures f, pae.photos p WHERE f.seller = ? "
          + "AND f.id_furniture = s.id_furniture AND p.id_photo = f.favorite_photo;";
      ps = dalBackendService.getPreparedStatement(sql);
      ps.setInt(1, id);
      ResultSet rs = ps.executeQuery();
      SaleDTO sale = null;
      while (rs.next()) {
        SaleDTO saleDTO = setTransaction(rs, sale);
        list.add(saleDTO);
      }
    } catch (SQLException e) {
      throw new FatalException(e);
    }
    return list;
  }

  private SaleDTO setTransaction(ResultSet rs, SaleDTO sale) {
    sale = new SaleImpl();
    FurnitureDTO furniture = furnitureFactory.getFurnitureDTO();
    try {
      sale.setId(rs.getInt(1));
      sale.setSellingPrice(rs.getDouble(2));
      sale.setIdFurniture(3);
      sale.setIdBuyer(rs.getInt(4));
      sale.setDateOfSale(rs.getTimestamp(5) == null ? null : rs.getTimestamp(5).toLocalDateTime());

      furniture.setId(rs.getInt(6));

      furniture.setDescription(rs.getString(7));
      furniture.setPurchasePrice(rs.getDouble(8));
      furniture
          .setPickUpDate(rs.getTimestamp(9) == null ? null : rs.getTimestamp(9).toLocalDateTime());
      furniture.setStoreDeposit(rs.getBoolean(10));
      furniture.setDepositDate(
          rs.getTimestamp(11) == null ? null : rs.getTimestamp(11).toLocalDateTime());
      furniture.setOfferedSellingPrice(rs.getDouble(12));
      furniture.setTypeId(rs.getInt(13));
      furniture.setRequestForVisitId(rs.getInt(14));
      furniture.setSellerId(rs.getInt(15));
      furniture.setFavouritePhotoId(rs.getInt(16));
      furniture.setFavouritePhoto(rs.getString(17));
      
      } catch (SQLException e) {
      throw new FatalException(e);
    }
    sale.setFurniture(furniture);

    return sale;
  }



  @Override
  public List<SaleDTO> getSalesList() {
    List<SaleDTO> list = new ArrayList<SaleDTO>();
    try {
      String sql = "SELECT s.id_sales, s.selling_price, s.id_furniture, s.id_buyer, s.date_of_sale"
          + " FROM pae.sales s";

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
