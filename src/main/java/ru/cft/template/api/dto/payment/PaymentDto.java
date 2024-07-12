package ru.cft.template.api.dto.payment;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record PaymentDto(
        String paymentId,
        Long cost,
        Long ownerPhone,
        Long recipientPhone,
        String comment,
        int status,
        LocalDateTime time
) {
}
