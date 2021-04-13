package be.vinci.pae.services.dal;

public interface DalServices {

  void getBizzTransaction(boolean autoCommit);

  /**
   * Commit and close a transaction with auto-commit enabled. A FatalException is thrown if an
   * SQLException shows up.
   */
  void commitBizzTransaction();

  /**
   * Rollback a transaction with auto-commit enabled. A FatalException is thrown if an SQLException
   * shows up.
   */
  void rollbackBizzTransaction();

  /**
   * Close a transaction with auto-commit disabled. A FatalException is thrown if an SQLException
   * shows up.
   */
  void stopBizzTransaction();

  /**
   * Check if the current thread has a transaction.
   * 
   * @return true if yes, false if not
   */
  boolean hasBizzTransaction();

  /**
   * Check if the current thread'stransaction is in auto-commit or not. If the current thread has no
   * transaction, false will be returned.
   * 
   * @return true if yes, false if not or no transaction is active
   */
  boolean isBizzTransactionInAutoCommit();

}
