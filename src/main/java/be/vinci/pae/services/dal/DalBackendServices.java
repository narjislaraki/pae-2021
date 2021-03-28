package be.vinci.pae.services.dal;

import java.sql.PreparedStatement;

public interface DalBackendServices {

  /**
   * Allows you to have a PreparedStatement.
   * 
   * @param sql the sql request
   * @return a preparedStatement
   */
  PreparedStatement getPreparedStatement(String sql);

  PreparedStatement getPreparedStatementWithGeneratedReturn(String sql);
}
