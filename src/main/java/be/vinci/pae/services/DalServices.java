package be.vinci.pae.services;

import java.sql.PreparedStatement;

public interface DalServices {

  PreparedStatement getPreparedStatement(String sql);

}
