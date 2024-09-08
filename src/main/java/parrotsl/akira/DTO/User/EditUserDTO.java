package parrotsl.akira.DTO.User;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "DTO for editing an existing user")
public class EditUserDTO {

  @Size(min = 2, message = "First name must be at least 2 characters long")
  @Schema(description = "User's first name", example = "John")
  private String firstName;

  @Size(min = 2, message = "Last name must be at least 2 characters long")
  @Schema(description = "User's last name", example = "Doe")
  private String lastName;

  @Size(min = 3, message = "Username must be at least 3 characters long")
  @Schema(description = "Username chosen by the user", example = "johndoe")
  private String username;

  @Email(message = "Email should be valid")
  @Schema(description = "User's email address", example = "john.doe@example.com")
  private String email;

  @Schema(description = "URL of the user's display picture", example = "https://example.com/profile.jpg")
  private String displayPicture;
}
