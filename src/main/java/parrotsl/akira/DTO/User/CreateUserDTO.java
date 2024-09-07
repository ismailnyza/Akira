package parrotsl.akira.DTO.User;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class CreateUserDTO {

  @NotNull(message = "First Name Cannot be null")
  private String firstName;
  private String lastName;
  private String displayName;
  private String email;
  private String displayPicture;
  private LocalDate dateOfBirth;
}
