package x0j3m.virtualwardrobe.web;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import x0j3m.virtualwardrobe.model.Role;
import x0j3m.virtualwardrobe.model.User;
import x0j3m.virtualwardrobe.service.UserService;
import x0j3m.virtualwardrobe.web.dto.LoginDTO;
import x0j3m.virtualwardrobe.web.dto.UserRequestDTO;

import java.net.URI;

@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<UserRequestDTO> getUserInfo(@AuthenticationPrincipal UserDetails userDetails) {
        try {
            String username = userDetails.getUsername();
            User user = userService.getUser(username);
            if (user == null) {
                return ResponseEntity.notFound().build();
            }

            UserRequestDTO dto = new UserRequestDTO(
                    user.getUsername(),
                    user.getFirstName(),
                    user.getLastName(),
                    user.getEmail(),
                    user.getRole()
            );
            return ResponseEntity.ok(dto);
        } catch (IllegalArgumentException | NullPointerException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/register")
    public ResponseEntity<Void> registerUser(@RequestBody User user) {
        try {
            user.setRole(Role.USER);
            long id = userService.saveUser(user);
            URI location = URI.create("/user/" + id);
            return ResponseEntity.created(location).build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody LoginDTO dto) {
        try {
            String jwtToken = userService.verify(dto);
            if (jwtToken == null) {
                return ResponseEntity.badRequest().build();
            }
            return ResponseEntity.ok(jwtToken);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }

    }

    @DeleteMapping
    public ResponseEntity<Void> deleteUser(@AuthenticationPrincipal UserDetails userDetails) {
        try {
            String username = userDetails.getUsername();
            User user = userService.getUser(username);
            if (user == null) {
                return ResponseEntity.notFound().build();
            }
            userService.deleteUser(user.getId());
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException | NullPointerException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping
    public ResponseEntity<Void> updateUser(@AuthenticationPrincipal UserDetails userDetails, @RequestBody User update) {
        try {
            String username = userDetails.getUsername();
            User user = userService.getUser(username);
            if (user == null) {
                return ResponseEntity.notFound().build();
            }

            User updated = userService.updateUser(user.getId(), update);
            URI location = URI.create("/user/" + updated.getId());
            return ResponseEntity.created(location).build();
        } catch (IllegalArgumentException | NullPointerException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
