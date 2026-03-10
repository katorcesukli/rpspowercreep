package org.personal.rpspowercreep.model.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LoginRequest(
        @NotBlank(message = "username is required")
        @Email(message = "username must be in the correct format")
        String username,

        @NotBlank(message = "password is required")
        @Size(min = 4, message = "password must be at least 4 characters")
        String password
) {
}
