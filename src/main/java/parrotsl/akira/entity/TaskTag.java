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

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "TaskTag")
@Schema(description = "Entity representing a tag for tasks")
public class TaskTag {

  @Id
  @GeneratedValue(strategy = GenerationType.TABLE)
  @Schema(description = "Unique identifier for the task tag", example = "1")
  private Long id;

  @Schema(description = "Name of the task tag", example = "Urgent")
  private String tagName;
}
