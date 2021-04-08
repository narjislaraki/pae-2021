package be.vinci.pae.domaine.visit;

public class VisitFactoryImpl implements VisitFactory {

  @Override
  public VisitDTO getVisitDTO() {
    return new VisitImpl();
  }
}
