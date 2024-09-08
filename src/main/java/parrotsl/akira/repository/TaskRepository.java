package parrotsl.akira.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import parrotsl.akira.entity.Task;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long>, JpaSpecificationExecutor<Task> {

  // Custom query to get all tasks by parentId
  @Query("SELECT t FROM Task t WHERE t.parentId = :parentId")
  Optional<List<Task>> findAllByParentTaskId( Long parentId);

//  get all tasks assigned To CurrentUser

  // Method to delete task by ID
  void deleteById(Long taskId);

  // Method to delete all tasks by parentId
  @Modifying
  @Query("DELETE FROM Task t WHERE t.parentId = :parentId")
  void deleteAllByParentTaskId( Long parentId);
}

