package x0j3m.virtualwardrobe.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import x0j3m.virtualwardrobe.model.Role;
import x0j3m.virtualwardrobe.model.User;
import x0j3m.virtualwardrobe.service.UserService;

@WebMvcTest(controllers = UserController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
@WithMockUser(username = "username")
public class UserControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @TestConfiguration
    static class TestConfig {
        @Bean
        UserService userService() {
            return Mockito.mock(UserService.class);
        }
    }

    @Autowired
    private UserService userService;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getUserInfo_whenUserInfoExists_shouldReturnOkStatus() throws Exception {
        User user = new User(1L, "username", "password", "firstName", "lastName", "email");
        Mockito.when(userService.getUser(Mockito.anyString())).thenReturn(user);

        ResultActions response = mockMvc.perform(
                MockMvcRequestBuilders.get("/user")
        );

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.username").value(user.getUsername()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value(user.getFirstName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName").value(user.getLastName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value(user.getEmail()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.role").value(user.getRole().name()));
    }

    @Test
    void getUserInfo_whenUserInfoDoesNotExist_shouldReturnNotFoundStatus() throws Exception {
        Mockito.when(userService.getUser(Mockito.anyString())).thenReturn(null);

        ResultActions response = mockMvc.perform(
                MockMvcRequestBuilders.get("/user")
        );

        response.andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void registerUser_whenUserExists_shouldReturnBadRequestStatus() throws Exception {
        User user = new User("username", "password", "firstName", "lastName", "email");

        Mockito.when(userService.saveUser(Mockito.any(User.class))).thenThrow(new IllegalArgumentException());

        ResultActions response = mockMvc.perform(
                MockMvcRequestBuilders.post("/user/register")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user))
        );

        response.andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void registerUser_whenUserDoesNotExist_shouldReturnCreatedStatus() throws Exception {
        User user = new User("username", "password", "firstName", "lastName", "email");

        Mockito.when(userService.saveUser(Mockito.any(User.class))).thenReturn(1L);

        ResultActions response = mockMvc.perform(
                MockMvcRequestBuilders.post("/user/register")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user))
        );

        response.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.header().string("Location", "/user/1"));
    }

    @Test
    void deleteUser_whenUserExists_shouldReturnNoContentStatus() throws Exception {
        User user = new User(1L, "username", "password", "firstName", "lastName", "email", Role.USER);

        Mockito.when(userService.getUser(Mockito.anyString())).thenReturn(user);
        Mockito.doNothing().when(userService).deleteUser(Mockito.anyLong());

        ResultActions response = mockMvc.perform(
                MockMvcRequestBuilders.delete("/user")
        );

        response.andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    void deleteUser_whenUserDoesNotExists_shouldReturnNotFoundStatus() throws Exception {
        Mockito.when(userService.getUser(Mockito.anyString())).thenReturn(null);

        ResultActions response = mockMvc.perform(
                MockMvcRequestBuilders.delete("/user")
        );

        response.andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void updateUser_whenUserDoesNotExists_shouldReturnNotFoundStatus() throws Exception {
        User update = User.builder().username("newUsername").password("newPassword").build();

        Mockito.when(userService.getUser(Mockito.anyString())).thenReturn(null);

        ResultActions response = mockMvc.perform(
                MockMvcRequestBuilders.patch("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(update))
        );

        response.andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void updateUser_whenUserExists_shouldReturnCreatedStatus() throws Exception {
        User user = new User(1L, "username", "password", "firstName", "lastName", "email", Role.USER);
        User update = User.builder().username("newUsername").password("newPassword").build();
        User updated = User.merge(user, update);

        Mockito.when(userService.getUser(Mockito.anyString())).thenReturn(user);
        Mockito.when(userService.updateUser(Mockito.anyLong(), Mockito.any(User.class))).thenReturn(updated);

        ResultActions response = mockMvc.perform(
                MockMvcRequestBuilders.patch("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(update))
        );

        response.andExpect(MockMvcResultMatchers.status().isCreated());
    }
}
