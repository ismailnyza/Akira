package parrotsl.akira.DTO.User;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import lombok.Data;

@Data
public class CreateUserDTO {

  @NotNull(message = "First Name Cannot be null")
  private String firstName;
  private String lastName;
  private String displayName;
  private String email;
  private String displayPicture;
  private LocalDate dateOfBirth;
}
