package parrotsl.akira.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import parrotsl.akira.DTO.User.CreateUserDTO;
import parrotsl.akira.service.UserService;
import parrotsl.akira.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import parrotsl.akira.DTO.authentication.AuthenticationRequest;
import parrotsl.akira.DTO.authentication.AuthenticationResponse;
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

  @Autowired
  private UserService userService;

  // Login endpoint
  @PostMapping("login")
  public AuthenticationResponse login(@RequestBody AuthenticationRequest authRequest)
      throws Exception {
    return userService.getAuthenticationResponse(authRequest);
  }

  // Registration endpoint
  @PostMapping("register")
  public ResponseEntity<?> registerUser(@RequestBody CreateUserDTO createUserDTO) {
    return userService.registerUserFromRequest(createUserDTO);
  }

}
