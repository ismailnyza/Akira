package parrotsl.akira.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import parrotsl.akira.entitiy.Task;
import parrotsl.akira.service.TaskService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/task")
public class TaskController {

    private final TaskService taskService;

    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping
    public Optional<Task> createTask(@RequestBody Task task) {
        return taskService.createTask(task);
    }

    @GetMapping
    public Optional<List<Task>> getAllTasks() {
        return taskService.getAllTasks();
    }

    @GetMapping("/{taskId}") // Specify path with variable
    public Optional<Task> getTaskByID(@PathVariable Long taskId) { // Use @PathVariable
        return taskService.getTaskById(taskId);
    }

    @DeleteMapping("/{taskId}") // Specify path with variable
    public String deleteTaskByID(@PathVariable Long taskId) { // Use @PathVariable
            return taskService.deleteTaskById(taskId);
    }

    @PatchMapping("/{taskId}") // Specify path with variable
    public Optional<Task> editTaskById(@PathVariable Long taskId , @RequestBody Task task) { // Use @PathVariable
        return taskService.editTask(taskId , task);
    }

}
