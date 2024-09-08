package parrotsl.akira.serviceTests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import parrotsl.akira.customExceptions.NoTaskTagFoundException;
import parrotsl.akira.entity.TaskTag;
import parrotsl.akira.repository.TaskTagRepository;

import java.util.Optional;
import parrotsl.akira.service.TaskTagService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TaskTagServiceTest {

  @Mock
  private TaskTagRepository taskTagRepository;

  @InjectMocks
  private TaskTagService taskTagService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  // ST_001: Test for finding a task tag by name successfully
  @Test
  void testFindTaskTagByName_Success_ST_001() {
    TaskTag taskTag = new TaskTag();
    taskTag.setTagName("Urgent");

    when(taskTagRepository.findByTagName("Urgent")).thenReturn(Optional.of(taskTag));

    TaskTag foundTag = taskTagService.findTaskTagName("Urgent");

    assertNotNull(foundTag);
    assertEquals("Urgent", foundTag.getTagName());
    verify(taskTagRepository, times(1)).findByTagName("Urgent");
  }

  // ST_002: Test for finding a task tag that does not exist (throws exception)
  @Test
  void testFindTaskTagByName_NotFound_ST_002() {
    when(taskTagRepository.findByTagName("NonExistent")).thenReturn(Optional.empty());

    assertThrows(NoTaskTagFoundException.class, () -> {
      taskTagService.findTaskTagName("NonExistent");
    });

    verify(taskTagRepository, times(1)).findByTagName("NonExistent");
  }

  // ST_003: Test for creating a task tag successfully
  @Test
  void testCreateTaskTag_Success_ST_003() {
    TaskTag taskTag = new TaskTag();
    taskTag.setTagName("High Priority");

    when(taskTagRepository.save(any(TaskTag.class))).thenReturn(taskTag);

    TaskTag createdTag = taskTagService.createTaskTag("High Priority");

    assertNotNull(createdTag);
    assertEquals("High Priority", createdTag.getTagName());
    verify(taskTagRepository, times(1)).save(any(TaskTag.class));
  }
}
