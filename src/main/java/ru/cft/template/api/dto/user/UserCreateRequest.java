package ru.cft.template.api.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import org.springframework.format.annotation.DateTimeFormat;
import ru.cft.template.core.validation.ValidPhone;

import java.time.LocalDate;

@Builder
public record UserCreateRequest(

        @Schema(description = "Номер телефона", example = "79807673242")
        @NotNull
        @ValidPhone
        Long phone,

        @Schema(description = "Пароль", example = "ItIsVal1dPassword?!")
        @NotNull
        @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!?])[A-Za-z\\d!?]{8,64}$", message = "Invalid password format")
        String password,

        @Schema(description = "Имя", example = "Иван")
        @NotNull
        @Pattern(regexp = "^[А-Я][а-я]{0,49}$", message = "Invalid first name format")
        String firstName,

        @Schema(description = "Фамилия", example = "Иванов")
        @NotNull
        @Pattern(regexp = "^[А-Я][а-я]{0,49}$", message = "Invalid last name format")
        String lastName,

        @Schema(description = "Отчество", example = "Иванович")
        @Pattern(regexp = "^[А-Я][а-я]{0,49}$", message = "Invalid patronymic name format")
        String patronymicName,

        @Schema(description = "Адрес электронной почты", example = "shift@cft.ru")
        @Email(message = "Invalid email format")
        @NotNull
        String email,

        @NotNull
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        LocalDate birthday
) {
}
