package be.vinci.pae.domain;

public class AdresseImpl implements Adresse {

  private int id;
  private int boite;
  private String rue;
  private String numero;
  private String ville;
  private String codePostal;
  private String pays;

  @Override
  public int getId() {
    return id;
  }

  @Override
  public void setId(int id) {
    this.id = id;
  }

  @Override
  public int getBoite() {
    return boite;
  }

  @Override
  public void setBoite(int boite) {
    this.boite = boite;
  }

  @Override
  public String getRue() {
    return rue;
  }

  @Override
  public void setRue(String rue) {
    this.rue = rue;
  }

  @Override
  public String getNumero() {
    return numero;
  }

  @Override
  public void setNumero(String numero) {
    this.numero = numero;
  }

  @Override
  public String getVille() {
    return ville;
  }

  @Override
  public void setVille(String ville) {
    this.ville = ville;
  }

  @Override
  public String getCodePostal() {
    return codePostal;
  }

  @Override
  public void setCodePostal(String codePostal) {
    this.codePostal = codePostal;
  }

  @Override
  public String getPays() {
    return pays;
  }

  @Override
  public void setPays(String pays) {
    this.pays = pays;
  }


}
