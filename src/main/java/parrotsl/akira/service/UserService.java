package parrotsl.akira.service;

import java.time.LocalDate;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import parrotsl.akira.DTO.User.CreateUserDTO;
import parrotsl.akira.DTO.User.EditUserDTO;
import parrotsl.akira.DTO.authentication.AuthenticationRequest;
import parrotsl.akira.DTO.authentication.AuthenticationResponse;
import parrotsl.akira.customExceptions.IncorrectValuesProvidedException;
import parrotsl.akira.customExceptions.NoUsersFoundException;
import parrotsl.akira.customExceptions.UserNotFoundException;
import parrotsl.akira.entity.User;
import parrotsl.akira.repository.UserRepository;
import parrotsl.akira.utils.JwtUtil;

@Service
public class UserService {

  private static final Logger logger = LogManager.getLogger(UserService.class);

  @Autowired
  private final UserRepository userRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Autowired
  private AuthenticationManager authenticationManager;

  @Autowired
  private CustomUserDetailsService userDetailsService;

  @Autowired
  private JwtUtil jwtUtil;

  public UserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public User createUser(CreateUserDTO createUserDTO) {
    logger.info("Creating user with username: {}", createUserDTO.getUsername());

    // Email validation regex
    String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
    Pattern pattern = Pattern.compile(emailRegex);

    if (createUserDTO.getFirstName() == null || createUserDTO.getFirstName().length() < 3) {
      logger.error("Invalid first name: {}", createUserDTO.getFirstName());
      throw new IncorrectValuesProvidedException("First name must be at least 3 characters");
    }

    if (createUserDTO.getLastName() == null || createUserDTO.getLastName().length() < 3) {
      logger.error("Invalid last name: {}", createUserDTO.getLastName());
      throw new IncorrectValuesProvidedException("Last name must be at least 3 characters");
    }

    if (createUserDTO.getUsername() == null || createUserDTO.getUsername().length() < 3) {
      logger.error("Invalid username: {}", createUserDTO.getUsername());
      throw new IncorrectValuesProvidedException("Username must be at least 3 characters");
    }

    if (createUserDTO.getEmail() == null || !pattern.matcher(createUserDTO.getEmail()).matches()) {
      logger.error("Invalid email: {}", createUserDTO.getEmail());
      throw new IncorrectValuesProvidedException("Please provide a valid email address");
    }

    if (createUserDTO.getDateofBirth() == null || createUserDTO.getDateofBirth().isAfter(LocalDate.now())) {
      logger.error("Invalid date of birth: {}", createUserDTO.getDateofBirth());
      throw new IncorrectValuesProvidedException("Please provide a valid Date of Birth");
    }

    User user = new User();
    user.setFirstName(createUserDTO.getFirstName());
    user.setLastName(createUserDTO.getLastName());
    user.setUsername(createUserDTO.getUsername());
    user.setEmail(createUserDTO.getEmail());
    user.setDateOfBirth(createUserDTO.getDateofBirth());

    User savedUser = userRepository.save(user);
    logger.info("User created successfully with ID: {}", savedUser.getId());

    return savedUser;
  }

  public List<User> findAll() {
    logger.info("Fetching all users");

    List<User> users = userRepository.findAll();

    if (users.isEmpty()) {
      logger.warn("No users found in the system");
      throw new NoUsersFoundException("No users found in the system");
    }

    logger.info("Fetched {} users", users.size());
    return users;
  }

  public User findById(Long userId) {
    logger.info("Fetching user with ID: {}", userId);

    return userRepository.findById(userId)
        .orElseThrow(() -> {
          logger.error("User with ID {} not found", userId);
          return new UserNotFoundException("User with ID " + userId + " not found");
        });
  }

  public User editUser(Long userId, EditUserDTO editUserDTO) {
    logger.info("Editing user with ID: {}", userId);

    User userFromDB = userRepository.findById(userId)
        .orElseThrow(() -> new UserNotFoundException("User with ID " + userId + " not found"));

    if (editUserDTO.getDisplayPicture() == null && editUserDTO.getUsername() == null &&
        editUserDTO.getFirstName() == null && editUserDTO.getLastName() == null &&
        editUserDTO.getEmail() == null) {
      logger.error("All fields cannot be null for user ID: {}", userId);
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

    User updatedUser = userRepository.save(userFromDB);
    logger.info("User with ID: {} updated successfully", userId);

    return updatedUser;
  }

  public void deleteUser(Long userId) {
    logger.info("Deleting user with ID: {}", userId);

    User userFromDB = userRepository.findById(userId)
        .orElseThrow(() -> new UserNotFoundException("User with ID " + userId + " not found"));

    userRepository.deleteById(userId);
    logger.info("User with ID: {} deleted successfully", userId);
  }

  public ResponseEntity<String> registerUserFromRequest(CreateUserDTO createUserDTO) {
    logger.info("Registering user from request for username: {}", createUserDTO.getUsername());

    ResponseEntity<String> body = validateRegistrationRequest(createUserDTO);
    if (body != null) {
      logger.error("Registration failed for user: {}", createUserDTO.getUsername());
      return body;
    }

    User user = createUserFromDTO(createUserDTO);
    userRepository.save(user);

    logger.info("User registered successfully: {}", createUserDTO.getUsername());
    return ResponseEntity.ok("User registered successfully!");
  }

  private User createUserFromDTO(CreateUserDTO createUserDTO) {
    User user = new User();
    user.setFirstName(createUserDTO.getFirstName());
    user.setLastName(createUserDTO.getLastName());
    user.setEmail(createUserDTO.getEmail());
    user.setUsername(createUserDTO.getUsername());
    user.setPassword(passwordEncoder.encode(createUserDTO.getPassword()));
    user.setRole("USER");
    return user;
  }

  private ResponseEntity<String> validateRegistrationRequest(CreateUserDTO createUserDTO) {
    if (userRepository.findByUsername(createUserDTO.getUsername()) != null) {
      logger.warn("Username already exists: {}", createUserDTO.getUsername());
      return ResponseEntity.badRequest().body("Error: User already exists!");
    }

    if (userRepository.findByEmail(createUserDTO.getEmail()) != null) {
      logger.warn("Email already exists: {}", createUserDTO.getEmail());
      return ResponseEntity.badRequest().body("Error: Email already exists!");
    }

    return null;
  }

  public AuthenticationResponse getAuthenticationResponse(AuthenticationRequest authRequest) throws Exception {
    logger.info("Authenticating user: {}", authRequest.getUsername());

    try {
      authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())
      );
    } catch (BadCredentialsException e) {
      logger.error("Invalid credentials for user: {}", authRequest.getUsername());
      throw new Exception("Incorrect username or password", e);
    }

    final UserDetails userDetails = userDetailsService.loadUserByUsername(authRequest.getUsername());
    final String jwt = jwtUtil.generateToken(userDetails.getUsername());

    logger.info("Authentication successful for user: {}", authRequest.getUsername());
    return new AuthenticationResponse(jwt);
  }
}
