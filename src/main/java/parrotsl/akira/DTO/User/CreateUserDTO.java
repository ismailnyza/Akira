package parrotsl.akira.DTO.User;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class CreateUserDTO {
  private String firstName;
  private String lastName;
  private String email;
  private String password;
  private String username;
  private LocalDate dateofBirth;
}
