package parrotsl.akira.DTO.Task;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import lombok.Data;
import parrotsl.akira.entity.Task;
import parrotsl.akira.entity.enums.Priority;
import parrotsl.akira.entity.enums.Status;
import parrotsl.akira.entity.enums.TaskVisibility;

@Data
@Schema(description = "DTO for creating or editing a task")
public class CreateTaskDTO {

  @Schema(description = "Title of the task", example = "Finish API development", required = true)
  private String title;

  @Schema(description = "Detailed description of the task", example = "Develop and test the task creation API", required = false)
  private String description;

  @JsonFormat(shape = JsonFormat.Shape.STRING)
  @Schema(description = "Current status of the task", example = "IN_PROGRESS", required = false)
  private Status status;

  @Schema(description = "Tags associated with the task", example = "[\"Development\", \"Backend\"]", required = false)
  private Set<String> tags;

  @Schema(description = "Priority of the task", example = "HIGH", required = false)
  private String priority;

  @JsonFormat(shape = JsonFormat.Shape.STRING)
  @Schema(description = "Visibility of the task", example = "PUBLIC", required = false)
  private TaskVisibility taskVisibility;

  @Schema(description = "List of user IDs assigned to this task", example = "[1, 2, 3]", required = false)
  private List<Long> assigneeUserIds;

  @Schema(description = "List of subtasks under this task", required = false)
  private List<Long> subtasksIds; // Instead of the full Task object, we use IDs to refer to subtasks

  @Schema(description = "ID of the parent task if this is a subtask", required = false)
  private Long parentTaskId; // Use ID to refer to parent task

  @Schema(description = "Subtasks", required = false)
  private List<Task> subtasks;
}
