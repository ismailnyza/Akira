package parrotsl.akira.DTO.Task;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.ArrayList;
import java.util.Set;
import lombok.Data;
import parrotsl.akira.entity.Comment;
import parrotsl.akira.entity.Task;
import parrotsl.akira.entity.enums.Priority;
import parrotsl.akira.entity.enums.Status;
import parrotsl.akira.entity.enums.TaskVisibility;

@Data
@Schema(description = "DTO for creating a new task")
public class CreateTaskDTO {

  @Schema(description = "Title of the task", example = "Finish API development")
  private String title;

  @Schema(description = "Description of the task", example = "Develop and test the task creation API")
  private String description;

  @JsonFormat(shape = JsonFormat.Shape.STRING)
  @Schema(description = "Current status of the task", example = "IN_PROGRESS")
  private Status status;

  @Schema(description = "Tags associated with the task", example = "[\"Development\", \"Backend\"]")
  private Set<String> tags;

  @JsonFormat(shape = JsonFormat.Shape.STRING)
  @Schema(description = "Priority of the task", example = "HIGH")
  private Priority priority;

  @JsonFormat(shape = JsonFormat.Shape.STRING)
  @Schema(description = "Visibility of the task", example = "PUBLIC")
  private TaskVisibility taskVisibility;

  @Schema(description = "List of user IDs assigned to this task", example = "[1, 2, 3]")
  private ArrayList<Long> assigneeUserIds;

  @Schema(description = "Comments on the task")
  private Comment comments;

  @Schema(description = "List of subtasks under this task")
  private ArrayList<Task> subtasks;

  @Schema(description = "Parent task if this is a subtask")
  private Task parentTask;
}
