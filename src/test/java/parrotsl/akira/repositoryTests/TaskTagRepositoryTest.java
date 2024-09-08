package parrotsl.akira.repositoryTests;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import static org.junit.jupiter.api.Assertions.*;

import parrotsl.akira.entity.TaskTag;
import parrotsl.akira.repository.TaskTagRepository;

import java.util.Optional;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class TaskTagRepositoryTest {

  @Autowired
  private TaskTagRepository taskTagRepository;

  // Test for saving a task tag
  @Test
  void testSaveTaskTag() {
    TaskTag tag = new TaskTag();
    tag.setTagName("High Priority");

    TaskTag savedTag = taskTagRepository.save(tag);
    assertNotNull(savedTag);
    assertNotNull(savedTag.getId());
    assertEquals("High Priority", savedTag.getTagName());
  }

  // Test for finding a task tag by tag name
  @Test
  void testFindByTagName() {
    TaskTag tag = new TaskTag();
    tag.setTagName("Bug");

    taskTagRepository.save(tag);

    Optional<TaskTag> foundTag = taskTagRepository.findByTagName("Bug");
    assertTrue(foundTag.isPresent());
    assertEquals("Bug", foundTag.get().getTagName());
  }

  // Test for deleting a task tag by ID
  @Test
  void testDeleteTaskTagById() {
    TaskTag tag = new TaskTag();
    tag.setTagName("To Be Deleted");

    TaskTag savedTag = taskTagRepository.save(tag);
    taskTagRepository.deleteById(savedTag.getId());

    Optional<TaskTag> deletedTag = taskTagRepository.findById(savedTag.getId());
    assertTrue(deletedTag.isEmpty());
  }
}
