package be.vinci.pae.services.dao;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

import be.vinci.pae.domain.sale.SaleDTO;
import be.vinci.pae.exceptions.FatalException;
import be.vinci.pae.services.dal.DalBackendServices;
import jakarta.inject.Inject;

public class SaleDAOImpl implements SaleDAO {

	@Inject
	private DalBackendServices dalBackendService;

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
}
