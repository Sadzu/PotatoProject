package ru.cft.template.api;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Paths {
    //  ----- User's operations -----
    public static final String USERS = "/users";
    public static final String USER_ID = "/users/{id}";
    //  ----- Session's operations -----
    public static final String USERS_SESSIONS = "/users/sessions";
    public static final String USERS_SESSIONS_CURRENT = "/users/sessions/current";
    public static final String USERS_SESSIONS_ID = "/users/sessions";
    //  ----- Wallet's operations -----
    public static final String USERS_WALLETS = "/users/wallets";
    //  ----- Remittance's operations -----
    public static final String REMITTANCE_CREATE = "/users/remittance";
    public static final String REMITTANCE_INFO = "/users/remittance/{id}";
    public static final String REMITTANCES_HISTORY_OWNER = "/users/remittances/owner";
    public static final String REMITTANCES_HISTORY_RECIPIENT = "/users/remittances/recipient";
    //  ----- Payment's operations -----
    public static final String PAYMENT_CREATE = "/users/payment";
    public static final String PAYMENT_CANCEL = "/users/payment/cancel/{id}";
    public static final String PAYMENT_PAY = "/users/payment/pay/{id}";
    public static final String PAYMENT_INFO = "/users/payments/one/{id}";
    public static final String PAYMENTS_OWNER = "/users/payments/owner";
    public static final String PAYMENTS_RECIPIENT = "/users/payments/recipient";
    public static final String PAYMENTS_OWNER_FILTER = "/users/payments/owner/{status}";
    public static final String PAYMENTS_RECIPIENT_FILTER = "/users/payments/recipient/{status}";

}
