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
import parrotsl.akira.controllers.TagsController;
import parrotsl.akira.entity.TaskTag;
import parrotsl.akira.service.TaskTagService;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class TagsControllerTest {

  private MockMvc mockMvc;

  @Mock
  private TaskTagService taskTagService;

  @InjectMocks
  private TagsController tagsController;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    mockMvc = MockMvcBuilders.standaloneSetup(tagsController).build();
  }

  @Test
  void testCreateTaskTag_Success() throws Exception {
    TaskTag taskTag = new TaskTag();
    taskTag.setTagName("Important");

    when(taskTagService.createTaskTag(anyString())).thenReturn(taskTag);

    ObjectMapper objectMapper = new ObjectMapper();
    String taskTagJson = objectMapper.writeValueAsString(taskTag);

    mockMvc.perform(post("/api/Tags/createTag")
            .contentType(MediaType.APPLICATION_JSON)
            .content(taskTagJson))
        .andExpect(status().isOk());

    verify(taskTagService, times(1)).createTaskTag(anyString());
  }
}
