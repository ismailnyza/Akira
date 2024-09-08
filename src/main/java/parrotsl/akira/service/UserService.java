package parrotsl.akira.service;

import java.time.LocalDate;
import java.util.List;
import java.util.regex.Pattern;
import org.springframework.stereotype.Service;
import parrotsl.akira.DTO.User.CreateUserDTO;
import parrotsl.akira.DTO.User.EditUserDTO;
import parrotsl.akira.customExceptions.IncorrectValuesProvidedException;
import parrotsl.akira.customExceptions.NoUsersFoundException;
import parrotsl.akira.customExceptions.UserNotFoundException;
import parrotsl.akira.entity.User;
import parrotsl.akira.repository.UserRepository;

@Service
public class UserService {

  private final UserRepository userRepository;

  public UserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }


  public User createUser(CreateUserDTO createUserDTO) {

    // Email validation regex
    String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
    Pattern pattern = Pattern.compile(emailRegex);

    if (createUserDTO.getFirstName() == null || createUserDTO.getFirstName().length() < 3) {
      throw new IncorrectValuesProvidedException("FirstName must be at least 3 characters");
    }

    if (createUserDTO.getLastName() == null || createUserDTO.getLastName().length() < 3) {
      throw new IncorrectValuesProvidedException("LastName must be at least 3 characters");
    }

    if (createUserDTO.getUsername() == null || createUserDTO.getUsername().length() < 3) {
      throw new IncorrectValuesProvidedException("DisplayName must be at least 3 characters");
    }

    if (createUserDTO.getEmail() == null || !pattern.matcher(createUserDTO.getEmail()).matches()) {
      throw new IncorrectValuesProvidedException("Please provide a valid email address");
    }

    if (createUserDTO.getDateofBirth() == null || createUserDTO.getDateofBirth()
        .isAfter(LocalDate.now())) {
      throw new IncorrectValuesProvidedException("Please provide a valid Date of Birth");

    }
    User user = new User();
    user.setFirstName(createUserDTO.getFirstName());
    user.setLastName(createUserDTO.getLastName());
    user.setUsername(createUserDTO.getUsername());
    user.setEmail(createUserDTO.getEmail());
    user.setDateOfBirth(createUserDTO.getDateofBirth());
    return userRepository.save(user);
  }

  public List<User> findAll() {
    List<User> users = userRepository.findAll();

    if (users.isEmpty()) {
      throw new NoUsersFoundException("No users found in the system");
    }
    return users;
  }

  public User findById(Long userId) {
    return userRepository.findById(userId)
        .orElseThrow(() -> new UserNotFoundException("User with ID " + userId + " not found"));
  }

  public User editUser(Long userId, EditUserDTO editUserDTO) {

    User userFromDB = userRepository.findById(userId)
        .orElseThrow(() -> new UserNotFoundException("User with ID" + userId + " not found"));
//    check if any fields are there
    if (editUserDTO.getDisplayPicture() == null &&
        editUserDTO.getUsername() == null &&
        editUserDTO.getFirstName() == null &&
        editUserDTO.getLastName() == null &&
        editUserDTO.getEmail() == null) {

      throw new IncorrectValuesProvidedException("All fields cannot be null");
    }
    if (editUserDTO.getFirstName() != null) {
      userFromDB.setFirstName(editUserDTO.getFirstName());
    }
    if (editUserDTO.getLastName() != null) {
      userFromDB.setLastName(editUserDTO.getLastName());
    }
    if (editUserDTO.getUsername() != null) {
      userFromDB.setUsername(editUserDTO.getUsername());
    }
    if (editUserDTO.getEmail() != null) {
      userFromDB.setEmail(editUserDTO.getEmail());
    }
    if (editUserDTO.getDisplayPicture() != null) {
      userFromDB.setDisplayPicture(editUserDTO.getDisplayPicture());
    }

    return userRepository.save(userFromDB);
  }


  public void deleteUser(Long userId) {
    User userFromDB = userRepository.findById(userId)
        .orElseThrow(() -> new UserNotFoundException("User with ID" + userId + " not found"));

    userRepository.deleteById(userId);
  }

}
