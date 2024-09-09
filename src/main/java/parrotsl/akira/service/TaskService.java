package parrotsl.akira.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
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

    handleSubTasks(taskFromRequest, taskFromDatabase);

    Task responseFromRepository = taskRepository.save(taskFromDatabase);

    if (taskFromRequest.getSubtasks() != null && !taskFromRequest.getSubtasks().isEmpty()) {
      for (Task subtask : taskFromRequest.getSubtasks()) {
        subtask.setParentId(taskId);
        taskRepository.save(subtask);
      }
    }

    return responseFromRepository;
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
    if (createTaskDTO.getTitle() != null) {
      task.setTitle(createTaskDTO.getTitle());
    }
    if (createTaskDTO.getDescription() != null) {
      task.setDescription(createTaskDTO.getDescription());
    }
    task.setPriority(
        createTaskDTO.getPriority() != null ?
            Priority.valueOf(createTaskDTO.getPriority().toUpperCase()) :
            Priority.LOW
    );
    task.setStatus(
        createTaskDTO.getStatus() != null ?
            Status.valueOf(createTaskDTO.getStatus().toString().toUpperCase()) :
            Status.TODO
    );
    task.setTaskVisibility(
        createTaskDTO.getTaskVisibility() != null ?
            TaskVisibility.valueOf(createTaskDTO.getTaskVisibility().toString().toUpperCase()) :
            TaskVisibility.PUBLIC
    );
    if (createTaskDTO.getAssigneeUserIds() != null) {
      task.setAssigneeUserIds(createTaskDTO.getAssigneeUserIds());
    }
    if (createTaskDTO.getParentTaskId() != null) {
      task.setParentId(createTaskDTO.getParentTaskId());
    }
    task.setLastUpdatedAt(LocalDateTime.now());
    if (createTaskDTO.getTags() != null && !createTaskDTO.getTags().isEmpty()) {
      task.setTags(new HashSet<>(createTaskDTO.getTags()));
    }

//    handle subtasks

  }

  public Optional<Task> createTask(CreateTaskDTO createTaskDTO) {
//    get logged in users details

    String username = ((UserDetails) SecurityContextHolder.getContext().getAuthentication()
        .getPrincipal()).getUsername();
    User creator = userRepository.findByUsername(username);

    Task taskFromRequest = new Task();
    taskFromRequest.setCreatorUserId(creator.getId());
    BeanUtils.copyProperties(createTaskDTO, taskFromRequest);
    validateCreateTaskRequest(taskFromRequest);
    taskFromRequest.setTaskCreatedAt(LocalDateTime.now());
    taskFromRequest.setCreatorUserId(creator.getId());
    populateTaskFields(taskFromRequest, createTaskDTO);
    taskFromRequest = taskRepository.save(taskFromRequest);

    handleSubTasks(createTaskDTO, taskFromRequest);

    return Optional.of(taskFromRequest);
  }

  private void handleSubTasks(CreateTaskDTO createTaskDTO, Task taskFromRequest) {
//    createTaskDTO.setSubtasksIds(taskFromRequest.sub);
    if (!(createTaskDTO.getSubtasks() == null)) {
      List<Task> subtasks = createTaskDTO.getSubtasks();
      for (Task subtask : subtasks) {
        CreateTaskDTO createTaskDTOForSubTask = new CreateTaskDTO();
        BeanUtils.copyProperties(subtask, createTaskDTOForSubTask);
        createTask(createTaskDTOForSubTask);
//        taskRepository.save(subtask);
      }
    }
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
    // Get the current user's ID
    UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
        .getPrincipal();
    String username = userDetails.getUsername();
    User currentUser = userRepository.findByUsername(username);
    Long currentUserId = currentUser.getId();

    // Query tasks where the current user is an assignee
    return taskRepository.findTasksByAssigneeUserId(currentUserId);

  }
}
