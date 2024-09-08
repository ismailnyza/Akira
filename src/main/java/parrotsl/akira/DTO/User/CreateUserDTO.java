package parrotsl.akira.DTO.User;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
@Getter
@Setter
@Schema(description = "DTO for creating a new user")
public class CreateUserDTO {

  @NotNull(message = "First name is required")
  @Size(min = 2, message = "First name must be at least 2 characters long")
  @Schema(description = "User's first name", example = "John")
  private String firstName;

  @NotNull(message = "Last name is required")
  @Size(min = 2, message = "Last name must be at least 2 characters long")
  @Schema(description = "User's last name", example = "Doe")
  private String lastName;

  @NotNull(message = "Email is required")
  @Email(message = "Email should be valid")
  @Schema(description = "User's email address", example = "john.doe@example.com")
  private String email;

  @NotNull(message = "Password is required")
  @Size(min = 6, message = "Password must be at least 6 characters long")
  @Schema(description = "User's password", example = "securePassword123")
  private String password;

  @NotNull(message = "Username is required")
  @Size(min = 3, message = "Username must be at least 3 characters long")
  @Schema(description = "Username chosen by the user", example = "johndoe")
  private String username;

  @NotNull(message = "Date of birth is required")
  @Past(message = "Date of birth must be in the past")
  @Schema(description = "User's date of birth", example = "1990-05-15")
  private LocalDate dateofBirth;
}
