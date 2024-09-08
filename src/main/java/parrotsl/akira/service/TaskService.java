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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import parrotsl.akira.DTO.Task.CreateTaskDTO;
import parrotsl.akira.DTO.Task.GetTaskWithChildrenDTO;
import parrotsl.akira.DTO.User.GetUserDTO;
import parrotsl.akira.customExceptions.IncorrectValuesProvidedException;
import parrotsl.akira.customExceptions.NoTaskFoundException;
import parrotsl.akira.customExceptions.NoUsersFoundException;
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

  public String deleteTaskById(Long taskId) {
    Task task = taskRepository.findById(taskId)
        .orElseThrow(() -> new NoTaskFoundException("Task doesn't exist"));

    // Find and delete all subtasks with the parentId matching the taskId
    List<Task> subtasks = taskRepository.findAllByParentTaskId(taskId)
        .orElse(Collections.emptyList());
    subtasks.forEach(subtask -> taskRepository.deleteById(subtask.getId()));

    // Delete the parent task
    taskRepository.deleteById(taskId);

    return "Task with ID " + taskId + " and its subtasks deleted successfully.";
  }

  public Task editTask(Long taskId, CreateTaskDTO taskFromRequest) {
    Task taskFromDatabase = taskRepository.findById(taskId)
        .orElseThrow(() -> new NoTaskFoundException("Task Doesn't Exist"));
    populateTaskFields(taskFromDatabase, taskFromRequest);
    return taskRepository.save(taskFromDatabase);
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
      ArrayList<Long> validAssignees = new ArrayList<>();
      for (Long assignee : taskFromRequest.getAssigneeUserIds()) {
        userRepository.findById(assignee).ifPresent(user -> validAssignees.add(assignee));
      }
      taskFromRequest.setAssigneeUserIds(validAssignees);
    }
  }

  private void populateTaskFields(Task task, CreateTaskDTO createTaskDTO) {
    task.setTitle(createTaskDTO.getTitle());
    task.setDescription(createTaskDTO.getDescription());
    task.setPriority(Optional.ofNullable(createTaskDTO.getPriority()).orElse(Priority.LOW));
    task.setStatus(createTaskDTO.getStatus() != null ? Status.valueOf(
        String.valueOf(createTaskDTO.getStatus())) : Status.TODO);
    task.setTaskVisibility(createTaskDTO.getTaskVisibility() != null ? TaskVisibility.valueOf(
        String.valueOf(createTaskDTO.getTaskVisibility())) : TaskVisibility.PUBLIC);
    task.setAssigneeUserIds(
        createTaskDTO.getAssigneeUserIds()
    );
    task.setParentId(
        Optional.ofNullable(createTaskDTO.getParentTask()).map(Task::getId).orElse(null));
    task.setLastUpdatedAt(LocalDateTime.now());

    // Add tags if provided
    Set<String> taskTags = Optional.ofNullable(createTaskDTO.getTags()).map(HashSet::new)
        .orElse(new HashSet<>());
    task.setTags(taskTags);
  }

  public Optional<Task> createTask(CreateTaskDTO createTaskDTO) {
    String username = ((UserDetails) SecurityContextHolder.getContext().getAuthentication()
        .getPrincipal()).getUsername();
    User creator = userRepository.findByUsername(username);

    Task taskFromRequest = new Task();
    taskFromRequest.setCreatorUserId(creator.getId());
    BeanUtils.copyProperties(createTaskDTO, taskFromRequest);
    validateCreateTaskRequest(taskFromRequest);
    populateTaskFields(taskFromRequest, createTaskDTO);

    taskFromRequest = taskRepository.save(taskFromRequest);

    if (createTaskDTO.getSubtasks() != null) {
      for (Task subtask : createTaskDTO.getSubtasks()) {
        populateTaskFields(subtask, createTaskDTO);
        subtask.setParentId(taskFromRequest.getId());
        taskRepository.save(subtask);
      }
    }

    return Optional.of(taskFromRequest);
  }

  public List<Task> getAllTasks() {
    return taskRepository.findAll();
  }

  public Task getTaskById(Long taskId) {
    return taskRepository.findById(taskId)
        .orElseThrow(() -> new NoTaskFoundException("Task Doesn't Exist"));
  }

  public GetTaskWithChildrenDTO getDetailedTasksById(Long taskId) {
    Task mainTask = taskRepository.findById(taskId)
        .orElseThrow(() -> new NoTaskFoundException("Task Doesn't Exist"));

    GetTaskWithChildrenDTO response = new GetTaskWithChildrenDTO();
    BeanUtils.copyProperties(mainTask, response);

    // Populate creator details
    userRepository.findById(mainTask.getCreatorUserId()).ifPresentOrElse(creator -> {
      GetUserDTO getUserDTO = new GetUserDTO();
      BeanUtils.copyProperties(creator, getUserDTO);
      response.setCreator(getUserDTO);
    }, () -> {
      throw new NoUsersFoundException("User with ID " + mainTask.getCreatorUserId() + " not found");
    });

    // Populate assignees
    // Fetch assignees based on user IDs
    List<Long> userIds = mainTask.getAssigneeUserIds();
    Optional<List<User>> assigneesOptional = userRepository.findAllByIdIn(new ArrayList<>(userIds));

// Prepare a list for DTOs
    ArrayList<GetUserDTO> getTaskWithChildrenDTOList = new ArrayList<>();

// Convert each User to GetTaskWithChildrenDTO and add to the DTO list
    assigneesOptional.ifPresent(assignees -> {
      for (User user : assignees) {
        GetUserDTO dto = new GetUserDTO();
        BeanUtils.copyProperties(user, dto); // Copy properties from User to DTO
        getTaskWithChildrenDTOList.add(dto);
      }

      // Set the assignees in the response
      response.setAssignees(getTaskWithChildrenDTOList);
    });

    return response;

  }

  public List<Task> getAllTasksAssignedToCurrentUser() {
//    get tasks where assignee user id is current user

  }
}
