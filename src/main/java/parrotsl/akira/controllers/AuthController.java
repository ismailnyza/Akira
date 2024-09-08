package parrotsl.akira.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import parrotsl.akira.DTO.User.CreateUserDTO;
import parrotsl.akira.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import parrotsl.akira.DTO.authentication.AuthenticationRequest;
import parrotsl.akira.DTO.authentication.AuthenticationResponse;
import parrotsl.akira.entity.User;
import parrotsl.akira.repository.UserRepository;
import parrotsl.akira.service.CustomUserDetailsService;

@RestController
@RequestMapping("api/auth/")
public class AuthController {

  @Autowired
  private AuthenticationManager authenticationManager;

  @Autowired
  private CustomUserDetailsService userDetailsService;

  @Autowired
  private JwtUtil jwtUtil;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;

  // Authentication endpoint for JWT generation
  @PostMapping("authenticate")
  public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authRequest) throws Exception {
    try {
      authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())
      );
    } catch (BadCredentialsException e) {
      throw new Exception("Incorrect username or password", e);
    }

    final UserDetails userDetails = userDetailsService.loadUserByUsername(authRequest.getUsername());
    final String jwt = jwtUtil.generateToken(userDetails.getUsername());

    return ResponseEntity.ok(new AuthenticationResponse(jwt));
  }

  // Login endpoint
  @PostMapping("login")
  public AuthenticationResponse login(@RequestBody AuthenticationRequest authRequest) throws Exception {
    try {
      authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())
      );
    } catch (Exception e) {
      throw new Exception("Invalid credentials", e);
    }

    final UserDetails userDetails = userDetailsService.loadUserByUsername(authRequest.getUsername());
    final String jwt = jwtUtil.generateToken(userDetails.getUsername());

    return new AuthenticationResponse(jwt);
  }

  // Registration endpoint
  @PostMapping("register")
  public ResponseEntity<?> registerUser(@RequestBody CreateUserDTO createUserDTO) {
    if (userRepository.findByUsername(createUserDTO.getUsername()) != null) {
      return ResponseEntity.badRequest().body("Error: User already Exists!");
    }

    // Create new user and encode password
    User user = new User();
    user.setFirstName(createUserDTO.getFirstName());
    user.setLastName(createUserDTO.getLastName());
    user.setEmail(createUserDTO.getEmail());
    user.setUsername(createUserDTO.getUsername());
    user.setPassword(passwordEncoder.encode(createUserDTO.getPassword())); // Hash the password
    user.setRole("USER"); // Default role

    // Save the user
    userRepository.save(user);

    return ResponseEntity.ok("User registered successfully!");
  }
}
