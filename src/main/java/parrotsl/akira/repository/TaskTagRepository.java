package parrotsl.akira.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import parrotsl.akira.entity.TaskTag;

//i dont really need this
public interface TaskTagRepository extends JpaRepository<TaskTag, Long> {

  Optional<TaskTag> findByTagName(String tagName);

}
