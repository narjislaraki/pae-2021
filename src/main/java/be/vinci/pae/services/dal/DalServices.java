package be.vinci.pae.services.dal;

import java.sql.SQLException;

public interface DalServices {

  void getConnection(boolean autoCommit);

  void commitTransaction() throws SQLException;

  void commitTransactionAndContinue() throws SQLException;

  void rollbackTransaction();

}
