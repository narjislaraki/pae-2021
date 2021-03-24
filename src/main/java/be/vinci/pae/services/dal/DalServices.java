package be.vinci.pae.services.dal;

public interface DalServices {

  void startTransaction();

  void commitTransaction();

  void rollbackTransaction();

}
