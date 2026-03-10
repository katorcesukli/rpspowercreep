package org.personal.rpspowercreep.model.dto.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequest(

        @NotBlank(message = "username is required")
        @Size(max = 20, message = "Usernames can have up to 20 characters")
        String username,

        @NotBlank(message = "password is required")
        @Size(min = 4, message = "password must be at least 4 characters")
        String password
) {
}
