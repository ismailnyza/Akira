package parrotsl.akira.DTO.authentication;

import lombok.Data;

@Data
public class AuthenticationRequest {
  private String username;
  private String password;

}