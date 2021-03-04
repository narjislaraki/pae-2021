package be.vinci.pae.services;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.sql.PreparedStatement;

@JsonDeserialize(as = DalServicesImpl.class)
public interface DalServices {

  PreparedStatement getPreparedStatement(String sql);

}
