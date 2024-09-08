package parrotsl.akira.DTO.User;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "DTO for retrieving user details")
public class GetUserDTO {

  @Schema(description = "User's first name", example = "John")
  private String firstName;

  @Schema(description = "User's last name", example = "Doe")
  private String lastName;

  @Schema(description = "Username chosen by the user", example = "johndoe")
  private String username;

  @Schema(description = "User's email address", example = "john.doe@example.com")
  private String email;

  @Schema(description = "URL of the user's display picture", example = "https://example.com/profile.jpg")
  private String displayPicture;
}
