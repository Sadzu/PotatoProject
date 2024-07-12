package ru.cft.template.api.dto.remittance;

import lombok.Builder;
import ru.cft.template.core.validation.ValidCost;
import ru.cft.template.core.validation.ValidPhone;

@Builder
public record RemittanceCreateRequest(

        @ValidPhone
        Long recipientPhone,
        Long recipientWallet,

        @ValidCost
        Long cost
) {
}
