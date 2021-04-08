package be.vinci.pae.services.dal;

public interface DalServices {

  void getBizzTransaction(boolean autoCommit);

  void commitBizzTransaction();

  void commitTransactionAndContinue();

  void rollbackTransaction();

  void stopBizzTransaction();

  boolean hasTransaction();

}
