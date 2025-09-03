package x0j3m.virtualwardrobe.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.Collections;

@Data
@AllArgsConstructor
@NoArgsConstructor(force = true)
@Builder
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Username cannot be null")
    @NotBlank(message = "Username cannot be empty")
    @Size(min = 5, message = "Username must be at least 5 characters long")
    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @NotNull(message = "Password cannot be null")
    @NotBlank(message = "Password cannot be empty")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    @Column(name = "password", nullable = false)
    private String password;

    @NotNull(message = "First name cannot be null")
    @NotBlank(message = "First name cannot be blank")
    @Column(name = "first_name", nullable = false)
    private String firstName;

    @NotNull(message = "Last name cannot be null")
    @NotBlank(message = "Last name cannot be blank")
    @Column(name = "last_name", nullable = false)
    private String lastName;

    @NotNull(message = "Email cannot be null")
    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Email must be correctly formatted")
    @Column(name = "email", nullable = false, unique = true)
    private String email;
    @Column(name = "role", nullable = false)
    private Role role;

    public User(String username, String password, String firstName, String lastName, String email) {
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.role = Role.USER;
    }

    public User(Long id, String username, String password, String firstName, String lastName, String email) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.role = Role.USER;
    }

    public static User merge(User base, User update) {
        String username = update.getUsername() == null ? base.getUsername() : update.getUsername();
        String password = update.getPassword() == null ? base.getPassword() : update.getPassword();
        String firstName = update.getFirstName() == null ? base.getFirstName() : update.getFirstName();
        String lastName = update.getLastName() == null ? base.getLastName() : update.getLastName();
        String email = update.getEmail() == null ? base.getEmail() : update.getEmail();
        return new User(base.getId(), username, password, firstName, lastName, email, base.getRole());
    }
}
