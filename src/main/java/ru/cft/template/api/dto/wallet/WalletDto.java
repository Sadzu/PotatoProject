package ru.cft.template.api.dto.wallet;

import lombok.Builder;

@Builder
public record WalletDto(
        Long walletId,
        Long balance
) {
}
