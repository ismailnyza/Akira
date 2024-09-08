package parrotsl.akira.DTO.Task;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.ArrayList;
import java.util.Set;
import lombok.Data;
import parrotsl.akira.entity.Comment;
import parrotsl.akira.entity.Task;
import parrotsl.akira.entity.enums.Priority;
import parrotsl.akira.entity.enums.Status;
import parrotsl.akira.entity.enums.TaskVisibility;

@Data
public class CreateTaskDTO {

  private String title;
  private String description;

  @JsonFormat(shape = JsonFormat.Shape.STRING)
  private Status status;

  private Set<String> tags;

  @JsonFormat(shape = JsonFormat.Shape.STRING)
  private Priority priority;

  @JsonFormat(shape = JsonFormat.Shape.STRING)
  private TaskVisibility taskVisibility;

  private ArrayList<Long> assigneeUserIds;
  private Comment comments;
  private ArrayList<Task> subtasks;
  private Task parentTask;
}
