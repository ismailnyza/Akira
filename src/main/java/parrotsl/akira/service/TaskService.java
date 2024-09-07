package parrotsl.akira.service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import parrotsl.akira.DTO.Task.CreateTaskDTO;
import parrotsl.akira.customExceptions.IncorrectValuesProvidedException;
import parrotsl.akira.customExceptions.NoTaskFoundException;
import parrotsl.akira.customExceptions.UserNotFoundException;
import parrotsl.akira.entity.Task;
import parrotsl.akira.entity.User;
import parrotsl.akira.entity.enums.Priority;
import parrotsl.akira.entity.enums.Status;
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
    validateCreateTaskRequest(createTaskDTO);
    Task task = new Task();
    PopulateTaskFields(task, createTaskDTO);
    return Optional.of(taskRepository.save(task));
  }


  public Optional<List<Task>> getAllTasks() {
//todo do i need an exception here?
    List<Task> allTasks = taskRepository.findAll();
    return Optional.of(allTasks);
  }

  public Task getTaskById(Long taskId) {
    //todo have separate files to store all exception messages
    return taskRepository.findById(taskId)
        .orElseThrow(() -> new NoTaskFoundException("Task Doesnt Exist"));
  }

  public String deleteTaskById(Long taskId) {
    Task task = taskRepository.findById(taskId)
        .orElseThrow(() -> new NoTaskFoundException("Task Doesnt Exist"));
    taskRepository.deleteById(taskId);
    return ("Task:" + task.getId() + " Deleted Successfully ");
  }

  public Task editTask(Long taskId, CreateTaskDTO taskfromRequest) {
    Task taskFromDatabase = taskRepository.findById(taskId)
        .orElseThrow(() -> new NoTaskFoundException("Task Doesnt Exist"));
    PopulateTaskFields(taskFromDatabase, taskfromRequest);
    taskRepository.save(taskFromDatabase);
    return taskFromDatabase;
  }

  //    todo write a specific Search finder
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

  private void validateCreateTaskRequest(CreateTaskDTO createTaskDTO) {
    if (createTaskDTO.getTitle() == null) {
      throw new IncorrectValuesProvidedException("please Provide a title to the Task");
    }

    if (createTaskDTO.getDescription() == null) {
//      todo move the whole thing to a validate request class with validation for each request
      throw new IncorrectValuesProvidedException("please Provide a description to the Task");
    }
//    check validity of each assigned user
    if (!createTaskDTO.getAssigneeUserIds().isEmpty()) {
      for (Long assignee : createTaskDTO.getAssigneeUserIds()) {
        userRepository.findById(assignee).orElseThrow(
            () -> new UserNotFoundException("Assignee to the " + assignee + " Task Doesn't Exist"));
      }
    }
//    todo fk this up
    if(!createTaskDTO.getTags().isEmpty()){
      for(String tag: createTaskDTO.getTags()){
          if(taskTagService.findTaskTagName(tag) == null){
             taskTagService.createTaskTag(tag);
          }
      }
    }
  }

  private void PopulateTaskFields(Task task, CreateTaskDTO createTaskDTO) {
    task.setTitle(createTaskDTO.getTitle());
    task.setDescription(createTaskDTO.getDescription());
    task.setPriority(
        createTaskDTO.getPriority() != null ? (Priority) createTaskDTO.getPriority()
            : Priority.Low);
    task.setCreatorUserId(
        createTaskDTO.getCreatorUserId() != null ? createTaskDTO.getCreatorUserId() : 6969);
    task.setSubtasks(createTaskDTO.getSubtasks() != null ? createTaskDTO.getSubtasks() : null);
    task.setParentTask(
        createTaskDTO.getParentTask() != null ? createTaskDTO.getParentTask() : null);
    task.setAssigneeUserIds(
        task.getAssigneeUserIds() != null ? task.getAssigneeUserIds() : Collections.singleton(
            task.getCreatorUserId()));
  }

}
