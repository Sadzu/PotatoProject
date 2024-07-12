create table if not exists public.users
(

    id              bigserial not null,
    phone           bigint    not null unique,
    birthday        date,
    enabled         boolean   not null,
    email           varchar   not null unique,
    password        varchar,
    first_name      varchar,
    last_name       varchar,
    patronymic_name varchar,
    creation_date   timestamp,
    update_date     timestamp,
    primary key (id)
);

create table if not exists public.session
(
    value           varchar(255),
    user_id         bigint references users (id),
    active          boolean not null,
    expiration_time timestamp,
    primary key (value)
);

create table if not exists public.wallets
(
    wallet_id bigserial not null unique,
    user_id   bigserial references users (id),
    balance   bigint check ( balance > 0 ),
    primary key (wallet_id)
)