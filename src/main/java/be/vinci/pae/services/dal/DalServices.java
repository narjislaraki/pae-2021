package be.vinci.pae.services.dal;

public interface DalServices {

  void getBizzTransaction(boolean autoCommit);

  void commitTransaction();

  void commitTransactionAndContinue();

  void rollbackTransaction();

  void stopBizzTransaction();

}
