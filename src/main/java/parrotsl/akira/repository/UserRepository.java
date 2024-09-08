package parrotsl.akira.repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import parrotsl.akira.entity.Task;
import parrotsl.akira.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

  User findByUsername(String displayName);

  Optional<User> findById(Long userId);

  Optional<List<User>> findAllByIdIn(List<Long> userIds);

}

