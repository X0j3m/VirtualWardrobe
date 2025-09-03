package x0j3m.virtualwardrobe.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import x0j3m.virtualwardrobe.data.UserRepository;
import x0j3m.virtualwardrobe.model.User;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class UserServiceTests {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void saveUser_whenUserIsValid_shouldReturnSavedUserId() {
        User user = new User("username", "password", "firstName", "lastName", "email@email.com");
        User save = new User(1L, user.getUsername(), user.getPassword(), user.getFirstName(), user.getLastName(), user.getEmail());

        Mockito.when(userRepository.findByUsername(Mockito.anyString())).thenReturn(Optional.empty());
        Mockito.when(userRepository.findByEmail(Mockito.anyString())).thenReturn(Optional.empty());
        Mockito.when(userRepository.save(Mockito.any(User.class))).thenReturn(save);

        Long id = userService.saveUser(user);

        Assertions.assertNotNull(id);
        Assertions.assertEquals(save.getId(), id);
    }

    @Test
    void saveUser_whenUserIsNull_shouldThrowIllegalArgumentException() {
        User user = null;
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            userService.saveUser(user);
        });
    }

    @Test
    void saveUser_whenAnyUserFieldIsNull_shouldThrowIllegalArgumentException() {
        User user = new User(null, null, null, null, null);
        Assertions.assertThrows(IllegalArgumentException.class, () -> userService.saveUser(user));
    }

    @Test
    void saveUser_whenUserUsernameIsAlreadyTaken_shouldThrowIllegalArgumentException() {
        User user = new User("username", "password1", "firstName1", "lastName1", "email1@email.com");
        User existingUser = new User(1L, "username", "password", "firstName", "lastName", "email@email.com");
        Optional<User> existingUserOptional = Optional.of(existingUser);

        Mockito.when(userRepository.findByUsername(Mockito.anyString())).thenReturn(existingUserOptional);

        Assertions.assertThrows(IllegalArgumentException.class, () -> userService.saveUser(user));
    }

    @Test
    void saveUser_whenEmailIsAlreadyTaken_shouldThrowIllegalArgumentException() {
        User user = new User("anotherUsername", "password1", "firstName1", "lastName1", "email@email.com");
        User existingUser = new User(1L, "username", "password", "firstName", "lastName", "email@email.com");
        Optional<User> existingUserOptional = Optional.of(existingUser);

        Mockito.when(userRepository.findByUsername(Mockito.anyString())).thenReturn(Optional.empty());
        Mockito.when(userRepository.findByEmail(Mockito.anyString())).thenReturn(existingUserOptional);

        Assertions.assertThrows(IllegalArgumentException.class, () -> userService.saveUser(user));
    }

    @Test
    void getUser_whenIdExists_shouldReturnUser() {
        User user = new User(1L, "username", "password", "firstName", "lastName", "email@email.com");

        Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(user));

        User found = userService.getUser(1L);

        Assertions.assertNotNull(found);
        Assertions.assertEquals(user.getId(), found.getId());
        Assertions.assertEquals(user.getUsername(), found.getUsername());
        Assertions.assertEquals(user.getPassword(), found.getPassword());
        Assertions.assertEquals(user.getFirstName(), found.getFirstName());
        Assertions.assertEquals(user.getLastName(), found.getLastName());
        Assertions.assertEquals(user.getEmail(), found.getEmail());
    }

    @Test
    void getUser_whenIdDoesNotExists_shouldReturnNull() {
        Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());

        User found = userService.getUser(999L);

        Assertions.assertNull(found);
    }

    @Test
    void getUser_whenIdIsNull_shouldThrowIllegalArgumentException() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> userService.getUser((Long) null));
    }

    @Test
    void getUser_whenIdIsZero_shouldThrowIllegalArgumentException() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> userService.getUser(0L));
    }

    @Test
    void getUser_whenIdIsNegative_shouldThrowIllegalArgumentException() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> userService.getUser(-1L));
    }

    @Test
    void getUser_whenUsernameExists_shouldReturnUser() {
        User user = new User(1L, "username", "password", "firstName", "lastName", "email@email.com");

        Mockito.when(userRepository.findByUsername(Mockito.anyString())).thenReturn(Optional.of(user));

        User found = userService.getUser("username");

        Assertions.assertNotNull(found);
        Assertions.assertEquals(user.getId(), found.getId());
        Assertions.assertEquals(user.getUsername(), found.getUsername());
        Assertions.assertEquals(user.getPassword(), found.getPassword());
        Assertions.assertEquals(user.getFirstName(), found.getFirstName());
        Assertions.assertEquals(user.getLastName(), found.getLastName());
        Assertions.assertEquals(user.getEmail(), found.getEmail());
    }

    @Test
    void getUser_whenUsernameDoesNotExists_shouldReturnNull() {
        Mockito.when(userRepository.findByUsername(Mockito.anyString())).thenReturn(Optional.empty());

        User found = userService.getUser("username");
        Assertions.assertNull(found);
    }

    @Test
    void getUser_whenUsernameIsNull_shouldThrowIllegalArgumentException() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> userService.getUser((String) null));
    }

    @Test
    void getUser_whenUsernameIsEmpty_shouldThrowIllegalArgumentException() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> userService.getUser(""));
    }

    @Test
    void deleteUser_whenIdExists_shouldDeleteUser() {
        Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(Mockito.mock(User.class)));
        Mockito.doNothing().when(userRepository).deleteById(Mockito.anyLong());

        Assertions.assertDoesNotThrow(() -> userService.deleteUser(1L));
    }

    @Test
    void deleteUser_whenIdDoesNotExists_shouldThrowIllegalArgumentException() {
        Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());

        Assertions.assertThrows(IllegalArgumentException.class, () -> userService.deleteUser(999L));
    }

    @Test
    void deleteUser_whenIdIsNull_shouldThrowIllegalArgumentException() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> userService.deleteUser(null));
    }

    @Test
    void deleteUser_whenIdIsZero_shouldThrowIllegalArgumentException() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> userService.deleteUser(0L));
    }

    @Test
    void deleteUser_whenIdIsNegative_shouldThrowIllegalArgumentException() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> userService.deleteUser(-1L));
    }

    @Test
    void updateUser_whenUserIdDoesNotExists_shouldThrowIllegalArgumentException() {
        User update = User.builder().username("username").build();
        Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());

        Assertions.assertThrows(IllegalArgumentException.class, () -> userService.updateUser(1L, update));
    }

    @Test
    void updateUser_whenUserIdExistsAndUpdateUsernameIsUnique_shouldReturnUpdatedUser() {
        User user = new User(1L, "username", "password", "firstName", "lastName", "email@email.com");
        User update = User.builder().username("newUsername").build();

        Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(user));
        Mockito.when(userRepository.save(Mockito.any(User.class))).thenReturn(User.merge(user, update));

        User updated = userService.updateUser(user.getId(), update);

        Assertions.assertNotNull(updated);
        Assertions.assertEquals(user.getId(), updated.getId());
        Assertions.assertEquals(update.getUsername(), updated.getUsername());
        Assertions.assertEquals(user.getPassword(), updated.getPassword());
        Assertions.assertEquals(user.getFirstName(), updated.getFirstName());
        Assertions.assertEquals(user.getLastName(), updated.getLastName());
        Assertions.assertEquals(user.getEmail(), updated.getEmail());
    }

    @Test
    void updateUser_whenUserIdExistsAndUpdateUsernameIsNotUnique_shouldReturnUpdatedUser() {
        User user = new User(1L, "username", "password", "firstName", "lastName", "email@email.com");
        User update = User.builder().username("newUsername").build();

        Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(user));
        Mockito.when(userRepository.save(Mockito.any(User.class))).thenThrow(new IllegalArgumentException("Username is already taken"));

        Assertions.assertThrows(IllegalArgumentException.class, () -> userService.updateUser(1L, update));
    }

    @Test
    void updateUser_whenUserIdExistsAndUpdateEmailIsUnique_shouldReturnUpdatedUser() {
        User user = new User(1L, "username", "password", "firstName", "lastName", "email@email.com");
        User update = User.builder().email("newEmail@email.com").build();

        Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(user));
        Mockito.when(userRepository.save(Mockito.any(User.class))).thenReturn(User.merge(user, update));

        User updated = userService.updateUser(user.getId(), update);

        Assertions.assertNotNull(updated);
        Assertions.assertEquals(user.getId(), updated.getId());
        Assertions.assertEquals(user.getUsername(), updated.getUsername());
        Assertions.assertEquals(user.getPassword(), updated.getPassword());
        Assertions.assertEquals(user.getFirstName(), updated.getFirstName());
        Assertions.assertEquals(user.getLastName(), updated.getLastName());
        Assertions.assertEquals(update.getEmail(), updated.getEmail());
    }

    @Test
    void updateUser_whenUserIdExistsAndUpdateEmailIsNotUnique_shouldReturnUpdatedUser() {
        User user = new User(1L, "username", "password", "firstName", "lastName", "email@email.com");
        User update = User.builder().email("newEmail@email.com").build();

        Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(user));
        Mockito.when(userRepository.save(Mockito.any(User.class))).thenThrow(new IllegalArgumentException("Email is already taken"));

        Assertions.assertThrows(IllegalArgumentException.class, () -> userService.updateUser(1L, update));
    }

    @Test
    void updateUser_whenIdIsNull_shouldThrowIllegalArgumentException() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> userService.updateUser(null, new User()));
    }

    @Test
    void updateUser_whenIdIsZero_shouldThrowIllegalArgumentException() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> userService.updateUser(0L, new User()));
    }

    @Test
    void updateUser_whenIdIsNegative_shouldThrowIllegalArgumentException() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> userService.updateUser(-1L, new User()));
    }

    @Test
    void updateUser_whenUpdateIsNull_shouldThrowIllegalArgumentException() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> userService.updateUser(1L, null));
    }

    @Test
    void updateUser_whenUpdateIsNotValid_shouldThrowIllegalArgumentException() {
        User user = new User(1L, "username", "password", "firstName", "lastName", "email@email.com");
        User update = User.builder().email("notValidEmail").build();

        Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(user));

        Assertions.assertThrows(IllegalArgumentException.class, () -> userService.updateUser(1L, update));
    }
}
