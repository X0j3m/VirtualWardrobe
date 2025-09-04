package x0j3m.virtualwardrobe.service;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import x0j3m.virtualwardrobe.data.UserRepository;
import x0j3m.virtualwardrobe.model.User;
import x0j3m.virtualwardrobe.web.dto.LoginDTO;

import java.util.Set;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private final Validator validator = factory.getValidator();

    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       AuthenticationManager authenticationManager,
                       JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    private void encodePassword(User user) {
        if (user.getPassword() == null) return;
        user.setPassword(passwordEncoder.encode(user.getPassword()));
    }

    public Long saveUser(User user) throws IllegalArgumentException {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }

        encodePassword(user);

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        if (violations.isEmpty()) {
            if (userRepository.findByUsername(user.getUsername()).isPresent()) {
                throw new IllegalArgumentException("User " + user.getUsername() + " is already taken");
            }
            if (userRepository.findByEmail(user.getEmail()).isPresent()) {
                throw new IllegalArgumentException("Email " + user.getEmail() + " is already taken");
            }
            User saved = userRepository.save(user);
            return saved.getId();
        } else {
            throw new IllegalArgumentException(violations.stream().findAny().get().getMessage());
        }
    }

    public User getUser(Long id) throws IllegalArgumentException {
        if (id == null) {
            throw new IllegalArgumentException("User id cannot be null");
        }
        if (id < 1) {
            throw new IllegalArgumentException("User id must be greater than 0");
        }
        return userRepository.findById(id).orElse(null);
    }

    public User getUser(String username) throws IllegalArgumentException {
        if (username == null || username.isEmpty()) {
            throw new IllegalArgumentException("Username cannot be null or empty");
        }
        return userRepository.findByUsername(username).orElse(null);
    }

    public void deleteUser(Long id) throws IllegalArgumentException {
        if (id == null) {
            throw new IllegalArgumentException("User id cannot be null");
        }
        if (id < 1) {
            throw new IllegalArgumentException("User id must be greater than 0");
        }
        if (userRepository.findById(id).isPresent()) {
            userRepository.deleteById(id);
        } else {
            throw new IllegalArgumentException("User id " + id + " does not exist");
        }
    }

    public User updateUser(Long id, User update) throws IllegalArgumentException {
        if (id == null) {
            throw new IllegalArgumentException("User id cannot be null");
        }
        if (id < 1) {
            throw new IllegalArgumentException("User id must be greater than 0");
        }
        if (update == null) {
            throw new IllegalArgumentException("User cannot be null");
        }

        encodePassword(update);

        User user = userRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("User id " + id + " does not exist")
        );

        User updatedUser = User.merge(user, update);
        Set<ConstraintViolation<User>> violations = validator.validate(updatedUser);

        if (violations.isEmpty()) {
            return userRepository.save(updatedUser);
        } else {
            throw new IllegalArgumentException(violations.stream().findAny().get().getMessage());
        }
    }

    public String verify(LoginDTO dto) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.login(), dto.password())
        );
        if (auth.isAuthenticated()) {
            return jwtService.generateToken(dto.login());
        }
        return null;
    }
}
