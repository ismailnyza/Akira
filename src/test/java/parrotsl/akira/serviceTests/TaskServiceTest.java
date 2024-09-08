package parrotsl.akira.serviceTests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import parrotsl.akira.DTO.Task.CreateTaskDTO;
import parrotsl.akira.customExceptions.NoTaskFoundException;
import parrotsl.akira.entity.Task;
import parrotsl.akira.repository.TaskRepository;
import parrotsl.akira.repository.UserRepository;

import java.util.Collections;
import java.util.Optional;
import parrotsl.akira.service.TaskService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TaskServiceTest {

  @Mock
  private TaskRepository taskRepository;

  @Mock
  private UserRepository userRepository;

  @InjectMocks
  private TaskService taskService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  // ST_001: Test for deleting a task successfully
  @Test
  void testDeleteTaskById_Success_ST_001() {
    Task task = new Task();
    task.setId(1L);

    when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
    when(taskRepository.findAllByParentTaskId(1L)).thenReturn(Optional.of(Collections.emptyList()));  // Fix: wrap in Optional

    String result = taskService.deleteTaskById(1L);

    assertEquals("Task with ID 1 and its subtasks deleted successfully.", result);
    verify(taskRepository, times(1)).findById(1L);
    verify(taskRepository, times(1)).deleteById(1L);
  }

  // ST_002: Test for deleting a task that does not exist (throws exception)
  @Test
  void testDeleteTaskById_NotFound_ST_002() {
    when(taskRepository.findById(1L)).thenReturn(Optional.empty());

    assertThrows(NoTaskFoundException.class, () -> {
      taskService.deleteTaskById(1L);
    });

    verify(taskRepository, times(1)).findById(1L);
    verify(taskRepository, never()).deleteById(1L);
  }

  // ST_003: Test for editing a task successfully
  @Test
  void testEditTask_Success_ST_003() {
    Task existingTask = new Task();
    existingTask.setId(1L);
    existingTask.setTitle("Old Task");

    CreateTaskDTO taskDTO = new CreateTaskDTO();
    taskDTO.setTitle("New Task");

    when(taskRepository.findById(1L)).thenReturn(Optional.of(existingTask));
    when(taskRepository.save(any(Task.class))).thenReturn(existingTask);

    Task editedTask = taskService.editTask(1L, taskDTO);

    assertNotNull(editedTask);
    assertEquals("New Task", editedTask.getTitle());
    verify(taskRepository, times(1)).save(any(Task.class));
  }

  // ST_004: Test for editing a task that does not exist (throws exception)
  @Test
  void testEditTask_NotFound_ST_004() {
    CreateTaskDTO taskDTO = new CreateTaskDTO();

    when(taskRepository.findById(1L)).thenReturn(Optional.empty());

    assertThrows(NoTaskFoundException.class, () -> {
      taskService.editTask(1L, taskDTO);
    });

    verify(taskRepository, times(1)).findById(1L);
    verify(taskRepository, never()).save(any(Task.class));
  }
}
