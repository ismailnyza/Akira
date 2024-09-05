package parrotsl.akira.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import parrotsl.akira.entity.Task;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

  Optional<Task> findById(Long taskId);

  void deleteById(Long taskId);

}
