package parrotsl.akira.controllerTests;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import parrotsl.akira.DTO.User.CreateUserDTO;
import parrotsl.akira.controllers.UserController;
import parrotsl.akira.entity.User;
import parrotsl.akira.service.UserService;

import java.util.Collections;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class UserControllerTest {

  private MockMvc mockMvc;

  @Mock
  private UserService userService;

  @InjectMocks
  private UserController userController;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
  }

  @Test
  void testCreateUser_Success() throws Exception {
    CreateUserDTO createUserDTO = new CreateUserDTO();
    createUserDTO.setFirstName("John");
    createUserDTO.setLastName("Doe");

    User user = new User();
    user.setFirstName("John");
    user.setLastName("Doe");

    when(userService.createUser(any(CreateUserDTO.class))).thenReturn(user);

    ObjectMapper objectMapper = new ObjectMapper();
    String userJson = objectMapper.writeValueAsString(createUserDTO);

    mockMvc.perform(post("/api/User/Create")
            .contentType(MediaType.APPLICATION_JSON)
            .content(userJson))
        .andExpect(status().isCreated());

    verify(userService, times(1)).createUser(any(CreateUserDTO.class));
  }

  @Test
  void testGetAllUsers_Success() throws Exception {
    when(userService.findAll()).thenReturn(Collections.emptyList());

    mockMvc.perform(get("/api/User/getallUsers"))
        .andExpect(status().isFound());

    verify(userService, times(1)).findAll();
  }

  @Test
  void testDeleteUser_Success() throws Exception {
    mockMvc.perform(delete("/api/User/1"))
        .andExpect(status().isOk());

    verify(userService, times(1)).deleteUser(1L);
  }
}
