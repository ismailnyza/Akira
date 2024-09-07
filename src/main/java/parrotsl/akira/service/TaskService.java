package parrotsl.akira.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import parrotsl.akira.DTO.User.CreateUserDTO;
import parrotsl.akira.entity.Task;
import parrotsl.akira.entity.User;
import parrotsl.akira.entity.enums.Priority;
import parrotsl.akira.entity.enums.Status;
import parrotsl.akira.repository.TaskRepository;

import java.util.List;
import java.util.Optional;
import parrotsl.akira.specifications.TaskSpecification;

@Service
@Transactional
public class TaskService {

  private final TaskRepository taskRepository;

  public TaskService(TaskRepository taskRepository) {
    this.taskRepository = taskRepository;
  }


  public Optional<Task> createTask(Task task) {
    //todo add logging
    System.out.println("Task Created:" + task.getTitle());
    return Optional.of(taskRepository.save(task));
  }

  public Optional<List<Task>> getAllTasks() {
    return Optional.of(taskRepository.findAll());
  }

  public Optional<Task> getTaskById(Long taskId) {
    return taskRepository.findById(taskId);
  }

  public String deleteTaskById(Long taskId) {
    Optional<Task> task = taskRepository.findById(taskId);
    if (task.isPresent()) {
      taskRepository.deleteById(taskId);
      return "Task " + taskId + " deleted Successfully";
    } else {
      return "No task found";
    }
  }

  public Optional<Task> editTask(Long taskId, Task task) {
    Optional<Task> taskFromDatabase = taskRepository.findById(taskId);
    if (taskFromDatabase.isPresent()) {
      Task taskUpdated = taskFromDatabase.get();
      if (task.getTitle() != null) {
        taskFromDatabase.get().setTitle(task.getTitle());
      }
      if (task.getDescription() != null) {
        taskFromDatabase.get().setDescription(task.getDescription());
      }
      taskRepository.save(task);
    }
    return taskFromDatabase;
  }

  //    todo write a specific Search finder
  public List<Task> searchTasks(String title, String description, User createdBy, User assignees,
      Status status, Priority priority) {
    Specification<Task> spec = Specification.where(null);

    if (title != null) {
      spec = spec.and(TaskSpecification.titleContains(title));
    }
    if (description != null) {
      spec = spec.and(TaskSpecification.descriptionContains(description));
    }
    if (createdBy != null) {
      spec = spec.and(TaskSpecification.createdBy(createdBy));
    }
    if (assignees != null) {
      spec = spec.and(TaskSpecification.assignedTo(assignees));
    }
    if (status != null) {
      spec = spec.and(TaskSpecification.statusIs(status));
    }
    if (priority != null) {
      spec = spec.and(TaskSpecification.priorityIs(priority));
    }

    return taskRepository.findAll(spec);
  }
}
