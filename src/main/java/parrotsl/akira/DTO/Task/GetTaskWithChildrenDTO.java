package parrotsl.akira.DTO.Task;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import lombok.Data;
import parrotsl.akira.DTO.User.GetUserDTO;
import parrotsl.akira.entity.Comment;
import parrotsl.akira.entity.Task;
import parrotsl.akira.entity.enums.Priority;
import parrotsl.akira.entity.enums.Status;
import parrotsl.akira.entity.enums.TaskVisibility;

@Data
@Schema(description = "DTO for retrieving a task along with its subtasks and additional details")
public class GetTaskWithChildrenDTO {

  @Schema(description = "Unique identifier of the task", example = "1")
  private Long id;

  @Schema(description = "Title of the task", example = "Develop new feature")
  private String title;

  @Schema(description = "Description of the task", example = "Develop and implement a new feature in the project")
  private String description;

  @JsonFormat(shape = JsonFormat.Shape.STRING)
  @Schema(description = "Current status of the task", example = "IN_PROGRESS")
  private Status status;

  @Schema(description = "Set of tags associated with the task", example = "[\"Feature\", \"Development\"]")
  private Set<String> tags;

  @Schema(description = "Details of the user who created the task")
  private GetUserDTO creator;

  @JsonFormat(shape = JsonFormat.Shape.STRING)
  @Schema(description = "Priority level of the task", example = "HIGH")
  private Priority priority;

  @JsonFormat(shape = JsonFormat.Shape.STRING)
  @Schema(description = "Visibility of the task", example = "PUBLIC")
  private TaskVisibility taskVisibility;

  @Schema(description = "List of users assigned to the task")
  private ArrayList<GetUserDTO> assignees;

  @Schema(description = "Comments associated with the task")
  private Comment comments;

  @Schema(description = "Subtasks associated with this task")
  private Optional<List<Task>> subtasks;
}
