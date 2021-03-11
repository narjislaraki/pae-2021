package be.vinci.pae.services.DAL;

import java.sql.PreparedStatement;

public interface DalServices {

  PreparedStatement getPreparedStatement(String sql);

}
