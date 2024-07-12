create table if not exists remittances
(
    remittance_id    bigint not null unique,
    owner            bigint references users (id),
    recipient_phone  bigint references users (id),
    recipient_wallet bigint references wallets (wallet_id),
    cost             bigint not null,
    time             timestamp,
    primary key (remittance_id)
)