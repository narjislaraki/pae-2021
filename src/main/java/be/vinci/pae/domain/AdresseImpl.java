package be.vinci.pae.domain;

public class AdresseImpl implements Adresse {

  private int id, boite;
  private String rue, numero, ville, code_postal, pays;

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
  public String getCode_postal() {
    return code_postal;
  }

  @Override
  public void setCode_postal(String code_postal) {
    this.code_postal = code_postal;
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
