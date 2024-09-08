package parrotsl.akira.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.springframework.beans.BeanUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import parrotsl.akira.DTO.Task.CreateTaskDTO;
import parrotsl.akira.DTO.Task.GetTaskWithChildrenDTO;
import parrotsl.akira.customExceptions.IncorrectValuesProvidedException;
import parrotsl.akira.customExceptions.NoTaskFoundException;
import parrotsl.akira.entity.Task;
import parrotsl.akira.entity.User;
import parrotsl.akira.entity.enums.Priority;
import parrotsl.akira.entity.enums.Status;
import parrotsl.akira.entity.enums.TaskVisibility;
import parrotsl.akira.repository.TaskRepository;
import parrotsl.akira.repository.UserRepository;
import parrotsl.akira.specifications.TaskSpecification;

@Service
@Transactional
public class TaskService {

  private final TaskRepository taskRepository;
  private final UserRepository userRepository;
  private final TaskTagService taskTagService;

  public TaskService(TaskRepository taskRepository, UserRepository userRepository,
      TaskTagService taskTagService) {
    this.taskRepository = taskRepository;
    this.userRepository = userRepository;
    this.taskTagService = taskTagService;
  }

  public Optional<Task> createTask(CreateTaskDTO createTaskDTO) {
    Task taskFromRequest = new Task();
    BeanUtils.copyProperties(createTaskDTO, taskFromRequest);
    validateCreateTaskRequest(taskFromRequest);
    populateTaskFields(taskFromRequest, createTaskDTO);
    taskFromRequest = taskRepository.save(taskFromRequest);
    if (createTaskDTO.getSubtasks() != null) {
      for (Task subtask : createTaskDTO.getSubtasks()) {
        populateTaskFields(subtask, createTaskDTO);
        subtask.setParentId(taskFromRequest.getId());
        Task savedSubtask = taskRepository.save(subtask); // Save the subtask
      }
    }
    return Optional.of(taskFromRequest);
  }

  public Optional<List<Task>> getAllTasks() {
    List<Task> allTasks = taskRepository.findAll();
    return Optional.of(allTasks);
  }

  public Task getTaskById(Long taskId) {
    return taskRepository.findById(taskId)
        .orElseThrow(() -> new NoTaskFoundException("Task Doesn't Exist"));
  }

  public GetTaskWithChildrenDTO getTaskByIdWithChildren(Long taskId) {
    Task mainTask = taskRepository.findById(taskId)
        .orElseThrow(() -> new NoTaskFoundException("Task Doesn't Exist"));
//    find if childrenExists

    Optional<List<Task>> subtasks = taskRepository.findAllByParentTaskId(taskId);
//    write an object to return this shit
    GetTaskWithChildrenDTO response = new GetTaskWithChildrenDTO();
    BeanUtils.copyProperties(mainTask , response);
    response.setSubtasks(subtasks);

    return response;
  }

  public String deleteTaskById(Long taskId) {
    Task task = taskRepository.findById(taskId)
        .orElseThrow(() -> new NoTaskFoundException("Task Doesn't Exist"));
    taskRepository.deleteById(taskId);
    return ("Task: " + task.getId() + " Deleted Successfully");
  }

  public Task editTask(Long taskId, CreateTaskDTO taskfromRequest) {
    Task taskFromDatabase = taskRepository.findById(taskId)
        .orElseThrow(() -> new NoTaskFoundException("Task Doesn't Exist"));
    populateTaskFields(taskFromDatabase, taskfromRequest);
    taskRepository.save(taskFromDatabase);
    return taskFromDatabase;
  }

  public List<Task> searchTasks(String title, String description, User createdBy, User assignees,
      Status status, Priority priority) {
    Specification<Task> taskSpecification = Specification.where(null);

    if (title != null) {
      taskSpecification = taskSpecification.and(TaskSpecification.titleContains(title));
    }
    if (description != null) {
      taskSpecification = taskSpecification.and(TaskSpecification.descriptionContains(description));
    }
    if (createdBy != null) {
      taskSpecification = taskSpecification.and(TaskSpecification.createdBy(createdBy));
    }
    if (assignees != null) {
      taskSpecification = taskSpecification.and(TaskSpecification.assignedTo(assignees));
    }
    if (status != null) {
      taskSpecification = taskSpecification.and(TaskSpecification.statusIs(status));
    }
    if (priority != null) {
      taskSpecification = taskSpecification.and(TaskSpecification.priorityIs(priority));
    }

    return taskRepository.findAll(taskSpecification);
  }

  private void validateCreateTaskRequest(Task taskFromRequest) {
    if (taskFromRequest.getTitle() == null) {
      throw new IncorrectValuesProvidedException("Please provide a title for the task");
    }
    if (taskFromRequest.getDescription() == null) {
      throw new IncorrectValuesProvidedException("Please provide a description for the task");
    }

    if (taskFromRequest.getAssigneeUserIds() != null) {
      Set<Long> validAssignees = new HashSet<>();
      for (Long assignee : taskFromRequest.getAssigneeUserIds()) {
        Optional<User> user = userRepository.findById(assignee);
        if (user.isPresent()) {
          validAssignees.add(assignee);
        }
      }
      taskFromRequest.setAssigneeUserIds(validAssignees);
    }
  }

  private void validateTask(Task task) {
    if (task.getTitle() == null) {
      throw new IncorrectValuesProvidedException("Please provide a title for the task");
    }
    if (task.getDescription() == null) {
      throw new IncorrectValuesProvidedException("Please provide a description for the task");
    }

    if (task.getAssigneeUserIds() != null) {
      Set<Long> validAssignees = new HashSet<>();
      for (Long assignee : task.getAssigneeUserIds()) {
        Optional<User> user = userRepository.findById(assignee);
        if (user.isPresent()) {
          validAssignees.add(assignee);
        }
      }
      task.setAssigneeUserIds(validAssignees);
    }
  }

  private void populateTaskFields(Task task, CreateTaskDTO createTaskDTO) {
    task.setTitle(createTaskDTO.getTitle());
    task.setDescription(createTaskDTO.getDescription());
    task.setPriority(
        createTaskDTO.getPriority() != null ? (Priority) createTaskDTO.getPriority()
            : Priority.LOW);
    task.setCreatorUserId(
        createTaskDTO.getCreatorUserId() != null ? createTaskDTO.getCreatorUserId() : 6969);
    task.setAssigneeUserIds(
        createTaskDTO.getAssigneeUserIds() != null ? createTaskDTO.getAssigneeUserIds()
            : Collections.singleton(task.getCreatorUserId()));
    task.setStatus(
        createTaskDTO.getStatus() != null ? Status.valueOf(createTaskDTO.getStatus().toString())
            : Status.TODO);
    task.setTaskCreatedAt(LocalDateTime.now());
    task.setTaskVisibility(createTaskDTO.getTaskVisibility() != null ? TaskVisibility.valueOf(
        createTaskDTO.getTaskVisibility().toString()) : TaskVisibility.PUBLIC);

    task.setParentId(
        createTaskDTO.getParentTask() != null ? createTaskDTO.getParentTask().getId() : null);

    task.setAssigneeUserIds(
        createTaskDTO.getAssigneeUserIds() != null ? createTaskDTO.getAssigneeUserIds()
            : Collections.singleton(task.getCreatorUserId()));
    task.setLastUpdatedAt(LocalDateTime.now());
    // Add tags if provided
    Set<String> taskTags = new HashSet<>();
    if (createTaskDTO.getTags() != null) {
      taskTags.addAll(createTaskDTO.getTags());
    }
    task.setTags(taskTags);
  }
}
