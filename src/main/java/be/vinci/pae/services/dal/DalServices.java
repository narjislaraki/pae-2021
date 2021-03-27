package be.vinci.pae.services.dal;

public interface DalServices {

  void getConnection(boolean autoCommit);

  void commitTransaction();

  void commitTransactionAndContinue();

  void rollbackTransaction();

}
