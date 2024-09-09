package parrotsl.akira.controllers;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import parrotsl.akira.DTO.User.CreateUserDTO;
import parrotsl.akira.DTO.User.EditUserDTO;
import parrotsl.akira.entity.User;
import parrotsl.akira.service.UserService;

@Controller
@RequestMapping("api/user")
public class UserController {

  @Autowired
  private final UserService userService;

  public UserController(UserService userService) {
    this.userService = userService;
  }

  //  create user allowed for everyone
  @PostMapping("/create")
  public ResponseEntity<User> createUser(@RequestBody CreateUserDTO createUserDTO) {
    User createdUser = userService.createUser(createUserDTO);
    return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
  }

  @GetMapping("/{userId}")
  public ResponseEntity<User> getUserById(@PathVariable Long userId) {
    User user = userService.findById(userId);
    return new ResponseEntity<>(user, HttpStatus.FOUND);
  }

  // todo can only be accessed by admin users
  @PatchMapping("/{userId}")
  public ResponseEntity<User> editUser(@PathVariable Long userId,
      @RequestBody EditUserDTO editUserDTO) {
    User user = userService.editUser(userId, editUserDTO);
    return new ResponseEntity<>(user, HttpStatus.ACCEPTED);
  }

  //  todo find based on filter criteria
  @GetMapping("/getallusers")
  public ResponseEntity<List<User>> getAllUsers() {
    List<User> users = userService.findAll();
    return new ResponseEntity<>(users, HttpStatus.FOUND);
  }

  @DeleteMapping("/{userId}")
  public ResponseEntity<String> deleteUserById(@PathVariable Long userId) {
    userService.deleteUser(userId);
    return new ResponseEntity<>("successfully Delete User with user Id " + userId, HttpStatus.OK);
  }

}
