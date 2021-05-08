package be.vinci.pae.factories;

import be.vinci.pae.domain.VisitImpl;
import be.vinci.pae.domain.interfaces.VisitDTO;
import be.vinci.pae.factories.interfaces.VisitFactory;

public class VisitFactoryImpl implements VisitFactory {

  @Override
  public VisitDTO getVisitDTO() {
    return new VisitImpl();
  }
}
