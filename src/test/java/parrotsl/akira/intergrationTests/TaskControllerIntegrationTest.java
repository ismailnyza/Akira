package parrotsl.akira.intergrationTests;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import parrotsl.akira.AkiraApplication;
import parrotsl.akira.entity.Task;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = AkiraApplication.class)
@AutoConfigureMockMvc
public class TaskControllerIntegrationTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  // 1. Test case for successful task creation
  @Test
  public void shouldCreateTaskSuccessfully() throws Exception {
    Task task = new Task();
    task.setTitle("Test Task");
    task.setDescription("This is a test task.");

    String taskJson = objectMapper.writeValueAsString(task);

    mockMvc.perform(post("/api/task")
            .contentType(MediaType.APPLICATION_JSON)
            .content(taskJson))
        .andExpect(status().isOk())  // Expecting 200 OK for successful task creation
        .andExpect(jsonPath("$.title").value("Test Task"))
        .andExpect(jsonPath("$.description").value("This is a test task."));
  }

  // 2. Test case for missing title
  @Test
  public void shouldReturnBadRequestWhenTitleIsMissing() throws Exception {
    Task task = new Task();
    task.setDescription("This is a task without a title.");

    String taskJson = objectMapper.writeValueAsString(task);

    mockMvc.perform(post("/api/task")
            .contentType(MediaType.APPLICATION_JSON)
            .content(taskJson))
        .andExpect(status().isBadRequest())  // Expecting 400 Bad Request
        .andExpect(jsonPath("$.message").value("Title is required"));  // Assuming the error message is structured this way
  }

  // 3. Test case for missing description
  @Test
  public void shouldReturnBadRequestWhenDescriptionIsMissing() throws Exception {
    Task task = new Task();
    task.setTitle("Test Task without Description");

    String taskJson = objectMapper.writeValueAsString(task);

    mockMvc.perform(post("/api/task")
            .contentType(MediaType.APPLICATION_JSON)
            .content(taskJson))
        .andExpect(status().isBadRequest())  // Expecting 400 Bad Request
        .andExpect(jsonPath("$.message").value("Description is required"));
  }

  // 4. Test case for missing both title and description
  @Test
  public void shouldReturnBadRequestWhenBothTitleAndDescriptionAreMissing() throws Exception {
    Task task = new Task();  // Neither title nor description is set

    String taskJson = objectMapper.writeValueAsString(task);

    mockMvc.perform(post("/api/task")
            .contentType(MediaType.APPLICATION_JSON)
            .content(taskJson))
        .andExpect(status().isBadRequest())  // Expecting 400 Bad Request
        .andExpect(jsonPath("$.message").value("Title and Description are required"));
  }
}
