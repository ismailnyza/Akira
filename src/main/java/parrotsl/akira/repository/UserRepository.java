package parrotsl.akira.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import parrotsl.akira.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User , Long> {

}
