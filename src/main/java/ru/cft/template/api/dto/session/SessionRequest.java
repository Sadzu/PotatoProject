package ru.cft.template.api.dto.session;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;

@Builder
public record SessionRequest(

        @Email(message = "Invalid email")
        @NotNull
        String email,

        @NotNull
        @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!?])[A-Za-z\\d!?]{8,64}$", message = "Invalid password")
        String password) {
}
