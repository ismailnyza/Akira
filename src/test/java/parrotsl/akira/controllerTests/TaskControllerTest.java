package parrotsl.akira.controllerTests;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import parrotsl.akira.DTO.Task.CreateTaskDTO;
import parrotsl.akira.controllers.TaskController;
import parrotsl.akira.entity.Task;
import parrotsl.akira.service.TaskService;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class TaskControllerTest {

  private MockMvc mockMvc;

  @Mock
  private TaskService taskService;

  @InjectMocks
  private TaskController taskController;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    mockMvc = MockMvcBuilders.standaloneSetup(taskController).build();
  }

  @Test
  void testCreateTask_Success() throws Exception {
    CreateTaskDTO createTaskDTO = new CreateTaskDTO();
    createTaskDTO.setTitle("New Task");

    Task task = new Task();
    task.setTitle("New Task");

    when(taskService.createTask(any(CreateTaskDTO.class))).thenReturn(Optional.of(task));

    ObjectMapper objectMapper = new ObjectMapper();
    String taskJson = objectMapper.writeValueAsString(createTaskDTO);

    mockMvc.perform(post("/api/task")
            .contentType(MediaType.APPLICATION_JSON)
            .content(taskJson))
        .andExpect(status().isOk());

    verify(taskService, times(1)).createTask(any(CreateTaskDTO.class));
  }

//  @Test
//  void testGetAllTasks_Success() throws Exception {
//    when(taskService.getAllTasks()).thenReturn(Collections.emptyList());
//
//    mockMvc.perform(get("/api/task"))
//        .andExpect(status().isOk());
//
//    verify(taskService, times(1)).getAllTasks();
//  }

  @Test
  void testDeleteTask_Success() throws Exception {
    when(taskService.deleteTaskById(1L)).thenReturn("Task deleted");

    mockMvc.perform(delete("/api/task/1"))
        .andExpect(status().isOk());

    verify(taskService, times(1)).deleteTaskById(1L);
  }
}
