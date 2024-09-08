package parrotsl.akira.repositoryTests;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import static org.junit.jupiter.api.Assertions.*;

import parrotsl.akira.entity.User;
import parrotsl.akira.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserRepositoryTest {

  @Autowired
  private UserRepository userRepository;

  // Test for saving a user
  @Test
  void testSaveUser() {
    User user = new User();
    user.setUsername("save_test");
    user.setEmail("save_test@example.com");

    User savedUser = userRepository.save(user);

    assertNotNull(savedUser);
    assertNotNull(savedUser.getId());
    assertEquals("save_test", savedUser.getUsername());
  }

  // Test for deleting a user by id
  @Test
  void testDeleteUserById() {
    User user = new User();
    user.setUsername("delete_test");
    user.setEmail("delete_test@example.com");

    User savedUser = userRepository.save(user);

    // Confirm the user exists
    assertTrue(userRepository.findById(savedUser.getId()).isPresent());

    // Delete the user
    userRepository.deleteById(savedUser.getId());

    // Confirm the user no longer exists
    assertFalse(userRepository.findById(savedUser.getId()).isPresent());
  }

  // Test for finding user by username
  @Test
  void testFindByUsername() {
    User user = new User();
    user.setUsername("john_doe");
    user.setEmail("john@example.com");

    userRepository.save(user);

    User foundUser = userRepository.findByUsername("john_doe");
    assertNotNull(foundUser);
    assertEquals("john@example.com", foundUser.getEmail());
  }

  // Test for finding user by id
  @Test
  void testFindById() {
    User user = new User();
    user.setUsername("jane_doe");
    user.setEmail("jane@example.com");

    User savedUser = userRepository.save(user);

    Optional<User> foundUser = userRepository.findById(savedUser.getId());
    assertTrue(foundUser.isPresent());
    assertEquals("jane@example.com", foundUser.get().getEmail());
  }

  // Test for finding a list of users by IDs
  @Test
  void testFindAllByIdIn() {
    User user1 = new User();
    user1.setUsername("user1");
    user1.setEmail("user1@example.com");

    User user2 = new User();
    user2.setUsername("user2");
    user2.setEmail("user2@example.com");

    userRepository.save(user1);
    userRepository.save(user2);

    List<Long> userIds = List.of(user1.getId(), user2.getId());
    Optional<List<User>> foundUsers = userRepository.findAllByIdIn(userIds);

    assertTrue(foundUsers.isPresent());
    assertEquals(2, foundUsers.get().size());
  }
}
