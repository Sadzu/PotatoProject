package ru.cft.template.api.dto.user;

import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Builder
public record UserPatchRequest(

        @Pattern(regexp = "^[А-Я][а-я]{0,49}$")
        String firstName,

        @Pattern(regexp = "^[А-Я][а-я]{0,49}$")
        String lastName,

        @Pattern(regexp = "^[А-Я][а-я]{0,49}$")
        String patronymicName,

        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        LocalDate birthday
) {
}
