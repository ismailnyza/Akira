package parrotsl.akira.repositoryTests;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

import parrotsl.akira.entity.Task;
import parrotsl.akira.repository.TaskRepository;

import java.util.List;
import java.util.Optional;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class TaskRepositoryTest {

  @Autowired
  private TaskRepository taskRepository;

  // Test for saving a task
  @Test
  void testSaveTask() {
    Task task = new Task();
    task.setTitle("Test Task");
    task.setParentId(null);

    Task savedTask = taskRepository.save(task);
    assertNotNull(savedTask);
    assertNotNull(savedTask.getId());
    assertEquals("Test Task", savedTask.getTitle());
  }

  // Test for finding tasks by parentId
  @Test
  void testFindAllByParentTaskId() {
    Task task = new Task();
    task.setTitle("Parent Task");
    task.setParentId(1L);
    taskRepository.save(task);

    Optional<List<Task>> foundTasks = taskRepository.findAllByParentTaskId(1L);
    assertTrue(foundTasks.isPresent());
    assertFalse(foundTasks.get().isEmpty());
  }

  // Test for deleting a task by ID
  @Test
  void testDeleteTaskById() {
    Task task = new Task();
    task.setTitle("Task to delete");
    Task savedTask = taskRepository.save(task);

    taskRepository.deleteById(savedTask.getId());
    Optional<Task> deletedTask = taskRepository.findById(savedTask.getId());
    assertTrue(deletedTask.isEmpty());
  }

  // Test for deleting all tasks by parentId
  @Test
  @Transactional
  void testDeleteAllByParentTaskId() {
    Task task1 = new Task();
    task1.setTitle("Task 1");
    task1.setParentId(2L);

    Task task2 = new Task();
    task2.setTitle("Task 2");
    task2.setParentId(2L);

    taskRepository.save(task1);
    taskRepository.save(task2);

    taskRepository.deleteAllByParentTaskId(2L);

    Optional<List<Task>> tasksAfterDeletion = taskRepository.findAllByParentTaskId(2L);
    assertTrue(tasksAfterDeletion.isEmpty());
  }

  // Test for finding tasks by assignee user ID
  @Test
  void testFindTasksByAssigneeUserId() {
    Task task = new Task();
    task.setTitle("Assigned Task");
    Long assigneeId = Long.MAX_VALUE;
    task.setAssigneeUserIds(List.of(assigneeId));
    taskRepository.save(task);

    List<Task> tasks = taskRepository.findTasksByAssigneeUserId(assigneeId);
    assertFalse(tasks.isEmpty());
    assertEquals(1, tasks.size());
    assertEquals("Assigned Task", tasks.get(0).getTitle());
  }
}
