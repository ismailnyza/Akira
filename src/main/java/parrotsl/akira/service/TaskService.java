package parrotsl.akira.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import parrotsl.akira.entitiy.Task;
import parrotsl.akira.repository.TaskRepository;

import java.util.List;
import java.util.Optional;

@Service
public class TaskService {

    private final TaskRepository taskRepository;

    @Autowired
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

}
