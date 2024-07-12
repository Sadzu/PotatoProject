create table if not exists payments
(
    payment_id          varchar        not null unique,
    cost                bigint         not null,
    owner_phone         bigint         references public.users (phone),
    recipient_phone     bigint         references public.users (phone),
    comment             varchar(250),
    status              integer        not null,
    time                timestamp      not null,
    primary key (payment_id)
)