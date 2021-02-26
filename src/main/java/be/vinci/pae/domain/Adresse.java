package be.vinci.pae.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(as = UtilisateurImpl.class)
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

  String getCode_postal();

  void setCode_postal(String code_postal);

  String getPays();

  void setPays(String pays);

}
