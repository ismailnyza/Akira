package parrotsl.akira.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    private Long id;
    private String firstName;
    private String lastName;
    private String displayName;
//    introduce a custom type
    private String email;
    private String displayPicture;
    private LocalDate dateOfBirth;
//    deprecated after keycloack
//    private Enum<Role> userRole;
//
}
