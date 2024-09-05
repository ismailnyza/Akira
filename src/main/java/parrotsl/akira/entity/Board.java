package parrotsl.akira.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "task-board")
@AllArgsConstructor
@NoArgsConstructor
public class Board {
    @Id
    private Long id;
    private Long taskId;

//    a task always belongs to a board

//    how to add dummy data when the session is start

}
