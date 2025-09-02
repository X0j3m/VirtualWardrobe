package x0j3m.virtualwardrobe.data;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import x0j3m.virtualwardrobe.model.User;

import java.util.Optional;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class UserRepositoryTests {
    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @Test
    void save_whenUserDoesNotExistsInDatabase_shouldReturnSavedUser() {
        User user = new User("username", "password", "firstName", "lastName", "email");

        User saved = userRepository.save(user);

        Assertions.assertNotNull(saved);
        Assertions.assertNotNull(saved.getId());
        Assertions.assertTrue(saved.getId() > 0);
        Assertions.assertEquals(user.getUsername(), saved.getUsername());
        Assertions.assertEquals(user.getPassword(), saved.getPassword());
        Assertions.assertEquals(user.getFirstName(), saved.getFirstName());
        Assertions.assertEquals(user.getLastName(), saved.getLastName());
        Assertions.assertEquals(user.getEmail(), saved.getEmail());
    }

    @Test
    void save_whenUserExistsInDatabase_shouldThrowDataIntegrityViolationException() {
        User user = new User("username", "password", "firstName", "lastName", "email");

        userRepository.save(user);

        User newUser = new User("username", "password1", "firstName1", "lastName1", "email1");

        Assertions.assertThrows(DataIntegrityViolationException.class, () -> userRepository.save(newUser));
    }

    @Test
    void save_whenAnyFieldIsUpdated_shouldReturnUpdatedUser() {
        User user = new User("username", "password", "firstName", "lastName", "email");

        User saved = userRepository.save(user);

        saved.setPassword("newPassword");

        User savedUserWithUpdatedPassword = userRepository.save(saved);

        Assertions.assertNotNull(savedUserWithUpdatedPassword);
        Assertions.assertNotNull(savedUserWithUpdatedPassword.getId());
        Assertions.assertEquals(saved.getId(), savedUserWithUpdatedPassword.getId());

        saved.setUsername("newUsername");

        User savedUserWithUpdatedUsername = userRepository.save(saved);

        Assertions.assertNotNull(savedUserWithUpdatedUsername);
        Assertions.assertNotNull(savedUserWithUpdatedUsername.getId());
        Assertions.assertEquals(saved.getId(), savedUserWithUpdatedUsername.getId());

        saved.setFirstName("newFirstName");

        User savedUserWithUpdatedFirstName = userRepository.save(saved);

        Assertions.assertNotNull(savedUserWithUpdatedFirstName);
        Assertions.assertNotNull(savedUserWithUpdatedFirstName.getId());
        Assertions.assertEquals(saved.getFirstName(), savedUserWithUpdatedFirstName.getFirstName());

        saved.setLastName("newLastName");

        User savedUserWithUpdatedLastName = userRepository.save(saved);

        Assertions.assertNotNull(savedUserWithUpdatedLastName);
        Assertions.assertNotNull(savedUserWithUpdatedLastName.getId());
        Assertions.assertEquals(saved.getId(), savedUserWithUpdatedLastName.getId());

        saved.setEmail("newEmail");

        User savedUserWithUpdatedEmail = userRepository.save(saved);

        Assertions.assertNotNull(savedUserWithUpdatedEmail);
        Assertions.assertNotNull(savedUserWithUpdatedEmail.getId());
        Assertions.assertEquals(saved.getId(), savedUserWithUpdatedEmail.getId());
    }

    @Test
    void findAll_whenTableIsEmpty_shouldReturnEmpty() {
        Iterable<User> allUsers = userRepository.findAll();

        Assertions.assertNotNull(allUsers);
        Assertions.assertFalse(allUsers.iterator().hasNext());
    }

    @Test
    void findAll_whenTableIsNotEmpty_shouldReturnAllSavedUsers() {
        User user1 = new User("username1", "password1", "firstName1", "lastName1", "email1");
        User user2 = new User("username2", "password2", "firstName2", "lastName2", "email2");
        User user3 = new User("username3", "password3", "firstName3", "lastName3", "email3");
        userRepository.save(user1);
        userRepository.save(user2);
        userRepository.save(user3);

        Iterable<User> allUsers = userRepository.findAll();

        Assertions.assertNotNull(allUsers);
        Assertions.assertEquals(3, allUsers.spliterator().getExactSizeIfKnown());
    }

    @Test
    void findById_whenUserExists_shouldReturnUser(){
        User user = new User("username", "password", "firstName", "lastName", "email");
        User saved = userRepository.save(user);

        long id = saved.getId();

        User found = userRepository.findById(id).orElse(null);

        Assertions.assertNotNull(found);
        Assertions.assertNotNull(found.getId());
        Assertions.assertTrue(found.getId() > 0);
        Assertions.assertEquals(user.getUsername(), found.getUsername());
        Assertions.assertEquals(user.getPassword(), found.getPassword());
        Assertions.assertEquals(user.getFirstName(), found.getFirstName());
        Assertions.assertEquals(user.getLastName(), found.getLastName());
        Assertions.assertEquals(user.getEmail(), found.getEmail());
    }

    @Test
    void findById_whenUserDoesNotExist_shouldReturnNull() {
        User user = userRepository.findById(999L).orElse(null);
        Assertions.assertNull(user);
    }

    @Test
    void findByUsername_whenUsernameExists_shouldReturnUser() {
        User user = new User("username", "password", "firstName", "lastName", "email");
        User saved = userRepository.save(user);

        String username = saved.getUsername();

        User found = userRepository.findByUsername(username);

        Assertions.assertNotNull(found);
        Assertions.assertNotNull(found.getId());
        Assertions.assertTrue(found.getId() > 0);
        Assertions.assertEquals(username, found.getUsername());
        Assertions.assertEquals(user.getPassword(), found.getPassword());
        Assertions.assertEquals(user.getFirstName(), found.getFirstName());
        Assertions.assertEquals(user.getLastName(), found.getLastName());
        Assertions.assertEquals(user.getEmail(), found.getEmail());
    }

    @Test
    void findByUsername_whenUsernameDoesNotExists_shouldReturnNull() {
        User found = userRepository.findByUsername("notExistingUsername");
        Assertions.assertNull(found);
    }

    @Test
    void deleteAll_whenTableIsNotEmpty_shouldDeleteAllUsers() {
        User user1 = new User("username1", "password1", "firstName1", "lastName1", "email1");
        User user2 = new User("username2", "password2", "firstName2", "lastName2", "email2");
        User user3 = new User("username3", "password3", "firstName3", "lastName3", "email3");
        userRepository.save(user1);
        userRepository.save(user2);
        userRepository.save(user3);

        userRepository.deleteAll();

        Iterable<User> allUsers = userRepository.findAll();

        Assertions.assertNotNull(allUsers);
        Assertions.assertFalse(allUsers.iterator().hasNext());
    }

    @Test
    void deleteAll_whenTableIsEmpty_shouldNotThrowException() {
        Assertions.assertDoesNotThrow(() -> userRepository.deleteAll());
    }

    @Test
    void deleteById_whenUserExists_shouldDeleteUser() {
        User user1 = new User("username1", "password1", "firstName1", "lastName1", "email1");
        User saved = userRepository.save(user1);

        long id = saved.getId();
        userRepository.deleteById(id);

        Assertions.assertNull(userRepository.findById(id).orElse(null));
    }

    @Test
    void deleteById_whenUserDoesNotExists_shouldNotThrowException() {
        Assertions.assertDoesNotThrow(() -> userRepository.deleteById(999L));
    }
}
