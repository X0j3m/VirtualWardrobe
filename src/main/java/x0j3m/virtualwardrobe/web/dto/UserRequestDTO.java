package x0j3m.virtualwardrobe.web.dto;

import x0j3m.virtualwardrobe.model.Role;

public record UserRequestDTO(
        String username,
        String firstName,
        String lastName,
        String email,
        Role role) {
}
