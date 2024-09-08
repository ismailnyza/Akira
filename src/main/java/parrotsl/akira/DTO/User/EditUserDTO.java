package parrotsl.akira.DTO.User;

import lombok.Data;

@Data
public class EditUserDTO {

  private String firstName;
  private String lastName;
  private String username;
  private String email;
  private String displayPicture;
}
