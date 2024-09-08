package parrotsl.akira.DTO.Task;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import lombok.Data;
import parrotsl.akira.entity.Comment;
import parrotsl.akira.entity.Task;
import parrotsl.akira.entity.enums.Priority;
import parrotsl.akira.entity.enums.Status;
import parrotsl.akira.entity.enums.TaskVisibility;

@Data
public class GetTaskWithChildrenDTO {

  private String title;
  private String description;

  @JsonFormat(shape = JsonFormat.Shape.STRING)
  private Status status;

  private Set<String> tags;
  private Long creatorUserId;

  @JsonFormat(shape = JsonFormat.Shape.STRING)
  private Priority priority;

  @JsonFormat(shape = JsonFormat.Shape.STRING)
  private TaskVisibility taskVisibility;

  private Set<Long> assigneeUserIds;
  private Comment comments;
  private Optional<List<Task>> subtasks;
}
