package parrotsl.akira.controllers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import parrotsl.akira.DTO.Task.CreateTaskDTO;
import parrotsl.akira.DTO.Task.GetTaskWithChildrenDTO;
import parrotsl.akira.entity.Task;
import parrotsl.akira.entity.User;
import parrotsl.akira.entity.enums.Priority;
import parrotsl.akira.entity.enums.Status;
import parrotsl.akira.service.TaskService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/task")
public class TaskController {

  private static final Logger logger = LogManager.getLogger(TaskController.class);

  private final TaskService taskService;

  @Autowired
  public TaskController(TaskService taskService) {
    this.taskService = taskService;
  }

  @PostMapping
  public Optional<Task> createTask(@RequestBody CreateTaskDTO createTaskDTO) {
    logger.info("Received request to create task: {}", createTaskDTO);
//    find and send the user id here
    Optional<Task> createdTask = taskService.createTask(createTaskDTO);
    logger.info("Task created successfully: {}", createdTask);
    return createdTask;
  }

  @GetMapping
  public List<Task> getAllTasks() {
    logger.info("Received request to get all tasks");
    List<Task> tasks = taskService.getAllTasks();
    logger.info("Returning {} tasks", tasks != null ? tasks.size() : 0);
    return tasks;
  }

  @GetMapping("/{taskId}")
  public Task getTaskByID(@PathVariable Long taskId) {
    logger.info("Received request to get task with ID: {}", taskId);
    Task task = taskService.getTaskById(taskId);
    logger.info("Task found: {}", task);
    return task;
  }

  @GetMapping("/allMe")
  public List<Task> getAllTasksAssignedToCurrentUser() {
    logger.info("Received request to get all tasks");
    List<Task> tasks = taskService.getAllTasksAssignedToCurrentUser();
    logger.info("Returning {} tasks", tasks != null ? tasks.size() : 0);
    return tasks;
  }

  @GetMapping("/withchildren/{taskId}")
  public GetTaskWithChildrenDTO getTaskWithChildrenByID(@PathVariable Long taskId) {
    logger.info("Received request to get task with ID: {}", taskId);
    GetTaskWithChildrenDTO task = taskService.getDetailedTasksById(taskId);
    logger.info("Task found: {}", task);
    return task;
  }

  @DeleteMapping("/{taskId}")
  public String deleteTaskByID(@PathVariable Long taskId) {
    logger.info("Received request to delete task with ID: {}", taskId);
    String response = taskService.deleteTaskById(taskId);
    logger.info("Task deleted successfully with ID: {}", taskId);
    return response;
  }

  @PatchMapping("/{taskId}")
  public Task editTaskById(@PathVariable Long taskId, @RequestBody CreateTaskDTO task) {
    logger.info("Received request to edit task with ID: {}", taskId);
    Task editedTask = taskService.editTask(taskId, task);
    logger.info("Task edited successfully: {}", editedTask);
    return editedTask;
  }

  @GetMapping("/search")
  public List<Task> searchTasks(
      @RequestParam(required = false) String title,
      @RequestParam(required = false) String description,
      @RequestParam(required = false) User createdBy,
      @RequestParam(required = false) User assignees,
      @RequestParam(required = false) Status status,
      @RequestParam(required = false) Priority priority) {

    logger.info("Received request to search tasks with filters - title: {}, description: {}, createdBy: {}, assignees: {}, status: {}, priority: {}",
        title, description, createdBy, assignees, status, priority);
    List<Task> tasks = taskService.searchTasks(title, description, createdBy, assignees, status, priority);
    logger.info("Search returned {} tasks", tasks.size());
    return tasks;
  }
}
