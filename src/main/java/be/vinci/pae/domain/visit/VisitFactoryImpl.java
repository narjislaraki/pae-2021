package be.vinci.pae.domain.visit;

public class VisitFactoryImpl implements VisitFactory {

  @Override
  public VisitDTO getVisitDTO() {
    return new VisitImpl();
  }
}
