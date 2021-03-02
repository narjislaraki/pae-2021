package be.vinci.pae.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(as = AdresseImpl.class)
public interface Adresse {

  int getId();

  void setId(int id);

  int getBoite();

  void setBoite(int boite);

  String getRue();

  void setRue(String rue);

  String getNumero();

  void setNumero(String numero);

  String getVille();

  void setVille(String ville);

  String getCodePostal();

  void setCodePostal(String codePostal);

  String getPays();

  void setPays(String pays);

}
