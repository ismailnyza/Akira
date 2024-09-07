package parrotsl.akira.DTO.Task;

import java.util.ArrayList;
import java.util.Set;
import lombok.Data;
import parrotsl.akira.entity.Comment;
import parrotsl.akira.entity.Task;
import parrotsl.akira.entity.User;
import parrotsl.akira.entity.enums.Priority;
import parrotsl.akira.entity.enums.Status;
import parrotsl.akira.entity.enums.TaskVisibility;

@Data
public class CreateTaskDTO {

  private String title;
  private String description;
  private Enum<Status> status;
  private Set<String> tags;
  private Long creatorUserId;
  private Enum<Priority> priority;
  private Enum<TaskVisibility> taskVisibility;
  private Set<Long> assigneeUserIds;
  private Comment comments;
  private ArrayList<Task> subtasks;
  private Task parentTask;
}
