package ru.cft.template.api.dto.payment;

import lombok.Builder;
import ru.cft.template.core.validation.ValidCost;
import ru.cft.template.core.validation.ValidPhone;

@Builder
public record PaymentCreateRequest(

        @ValidPhone
        Long recipientPhone,
        String comment,

        @ValidCost
        Long cost
) {
}
