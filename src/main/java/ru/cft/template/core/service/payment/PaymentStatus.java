package ru.cft.template.core.service.payment;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class PaymentStatus {
    public static final int NOT_PAYED = 0;
    public static final int PAYED = 1;
    public static final int CANCELED = -1;
}
