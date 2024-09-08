package parrotsl.akira.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Schema(description = "Entity representing a user in the system")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    @Schema(description = "Unique identifier for the user", example = "1")
    private Long id;

    @Schema(description = "User's first name", example = "John")
    private String firstName;

    @Schema(description = "User's last name", example = "Doe")
    private String lastName;

    @Schema(description = "User's password (hashed)", example = "hashedpassword")
    private String password;

    @Schema(description = "Username chosen by the user", example = "johndoe")
    private String username;

    @Schema(description = "Role assigned to the user", example = "ADMIN")
    private String role;

    @Schema(description = "User's email address", example = "john.doe@example.com")
    private String email;

    @Schema(description = "URL of the user's display picture", example = "https://example.com/profile.jpg")
    private String displayPicture;

    @Schema(description = "User's date of birth", example = "1990-05-15")
    private LocalDate dateOfBirth;
}
