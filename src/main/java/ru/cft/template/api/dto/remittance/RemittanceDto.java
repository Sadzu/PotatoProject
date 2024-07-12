package ru.cft.template.api.dto.remittance;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record RemittanceDto(
        Long remittanceId,
        Long owner,
        Long recipientPhone,
        Long recipientWallet,
        LocalDateTime time,
        Long cost
) {
}
