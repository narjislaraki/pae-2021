package be.vinci.pae.services.dal;

import java.sql.PreparedStatement;

public interface DalServices {

  PreparedStatement getPreparedStatement(String sql);

}
