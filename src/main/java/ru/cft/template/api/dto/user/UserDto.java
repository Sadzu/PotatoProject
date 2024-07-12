package ru.cft.template.api.dto.user;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record UserDto(
        Long id,
        String firstName,
        String lastName,
        String patronymicName,
        String email,
        int age,
        Long walletId,
        long phone,
        LocalDateTime registrationDate,
        LocalDateTime lastUpdateDate) {
}
