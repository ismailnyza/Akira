package parrotsl.akira.serviceTests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import parrotsl.akira.DTO.User.CreateUserDTO;
import parrotsl.akira.DTO.User.EditUserDTO;
import parrotsl.akira.customExceptions.IncorrectValuesProvidedException;
import parrotsl.akira.customExceptions.NoUsersFoundException;
import parrotsl.akira.customExceptions.UserNotFoundException;
import parrotsl.akira.entity.User;
import parrotsl.akira.repository.UserRepository;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import parrotsl.akira.service.UserService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

  @Mock
  private UserRepository userRepository;

  @InjectMocks
  private UserService userService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  // ST_001: Test for creating a user successfully
  @Test
  void testCreateUser_Success_ST_001() {
    CreateUserDTO createUserDTO = new CreateUserDTO();
    createUserDTO.setFirstName("John");
    createUserDTO.setLastName("Doe");
    createUserDTO.setUsername("john_doe");
    createUserDTO.setEmail("john.doe@example.com");
    createUserDTO.setDateofBirth(LocalDate.of(1990, 1, 1));

    User user = new User();
    user.setId(1L);
    user.setFirstName("John");
    user.setLastName("Doe");

    when(userRepository.save(any(User.class))).thenReturn(user);

    User createdUser = userService.createUser(createUserDTO);

    assertNotNull(createdUser);
    assertEquals("John", createdUser.getFirstName());
    verify(userRepository, times(1)).save(any(User.class));
  }

  // ST_002: Test for creating a user with invalid email
  @Test
  void testCreateUser_InvalidEmail_ST_002() {
    CreateUserDTO createUserDTO = new CreateUserDTO();
    createUserDTO.setFirstName("John");
    createUserDTO.setLastName("Doe");
    createUserDTO.setUsername("john_doe");
    createUserDTO.setEmail("invalid-email");
    createUserDTO.setDateofBirth(LocalDate.of(1990, 1, 1));

    assertThrows(IncorrectValuesProvidedException.class, () -> {
      userService.createUser(createUserDTO);
    });

    verify(userRepository, never()).save(any(User.class));
  }

  // ST_003: Test for finding all users successfully
  @Test
  void testFindAllUsers_Success_ST_003() {
    User user = new User();
    user.setId(1L);
    user.setFirstName("John");

    when(userRepository.findAll()).thenReturn(List.of(user));

    List<User> users = userService.findAll();
    assertFalse(users.isEmpty());
    verify(userRepository, times(1)).findAll();
  }

  // ST_004: Test for finding no users (throws exception)
  @Test
  void testFindAllUsers_NoUsersFound_ST_004() {
    when(userRepository.findAll()).thenReturn(Collections.emptyList());

    assertThrows(NoUsersFoundException.class, () -> {
      userService.findAll();
    });

    verify(userRepository, times(1)).findAll();
  }

  // ST_005: Test for finding a user by ID successfully
  @Test
  void testFindUserById_Success_ST_005() {
    User user = new User();
    user.setId(1L);
    user.setFirstName("John");

    when(userRepository.findById(1L)).thenReturn(Optional.of(user));

    User foundUser = userService.findById(1L);
    assertNotNull(foundUser);
    assertEquals(1L, foundUser.getId());
  }

  // ST_007: Test for editing a user successfully
  @Test
  void testEditUser_Success_ST_007() {
    User existingUser = new User();
    existingUser.setId(1L);
    existingUser.setFirstName("John");

    EditUserDTO editUserDTO = new EditUserDTO();
    editUserDTO.setFirstName("Johnny");

    when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));
    when(userRepository.save(any(User.class))).thenReturn(existingUser);

    User editedUser = userService.editUser(1L, editUserDTO);

    assertNotNull(editedUser);
    assertEquals("Johnny", editedUser.getFirstName());
    verify(userRepository, times(1)).save(any(User.class));
  }

  // ST_008: Test for deleting a user by ID successfully
  @Test
  void testDeleteUser_Success_ST_008() {
    User user = new User();
    user.setId(1L);
    when(userRepository.findById(1L)).thenReturn(Optional.of(user));

    userService.deleteUser(1L);
    verify(userRepository, times(1)).deleteById(1L);
  }

  // ST_009: Test for deleting a user when user is not found
  @Test
  void testDeleteUser_NotFound_ST_009() {
    when(userRepository.findById(1L)).thenReturn(Optional.empty());

    assertThrows(UserNotFoundException.class, () -> {
      userService.deleteUser(1L);
    });

    verify(userRepository, never()).deleteById(anyLong());
  }
}
