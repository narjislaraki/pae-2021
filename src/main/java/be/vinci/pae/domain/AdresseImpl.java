package be.vinci.pae.domain;

public class AdresseImpl {

  private int id, boite;
  private String rue, numero, ville, code_postal, pays;

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public int getBoite() {
    return boite;
  }

  public void setBoite(int boite) {
    this.boite = boite;
  }

  public String getRue() {
    return rue;
  }

  public void setRue(String rue) {
    this.rue = rue;
  }

  public String getNumero() {
    return numero;
  }

  public void setNumero(String numero) {
    this.numero = numero;
  }

  public String getVille() {
    return ville;
  }

  public void setVille(String ville) {
    this.ville = ville;
  }

  public String getCode_postal() {
    return code_postal;
  }

  public void setCode_postal(String code_postal) {
    this.code_postal = code_postal;
  }

  public String getPays() {
    return pays;
  }

  public void setPays(String pays) {
    this.pays = pays;
  }


}
