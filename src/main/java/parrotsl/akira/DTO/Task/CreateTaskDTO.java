package parrotsl.akira.DTO.Task;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import java.time.LocalDateTime;
import java.util.ArrayList;
import parrotsl.akira.entity.Comment;
import parrotsl.akira.entity.Task;
import parrotsl.akira.entity.User;
import parrotsl.akira.entity.enums.Priority;
import parrotsl.akira.entity.enums.Status;
import parrotsl.akira.entity.enums.TaskVisibility;

public class CreateTaskDTO {

  public String title;
  public Enum<Status> status;
  public Enum<Priority> priority;
  public User createdBy;
  public Enum<TaskVisibility> taskVisibility;
  public User assignees;
  public ArrayList<String> tags;
  public Comment comments;
  public Task subtasks;
  public String description;
}
