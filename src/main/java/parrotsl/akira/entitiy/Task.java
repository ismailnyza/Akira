package parrotsl.akira.entitiy;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tasks")
@Data
@AllArgsConstructor
@NoArgsConstructor

public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;
    public String title;
    public String description;

}
