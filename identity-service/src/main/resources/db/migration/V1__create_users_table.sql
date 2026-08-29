CREATE TABLE users
(
    id               uuid         not null,
    email            varchar(255) not null,
    normalized_email varchar(255) not null,
    password_hash    varchar(255) not null,
    display_name     varchar(255) not null,
    role             varchar(30)  not null default 'USER',
    status           varchar(30)  not null default 'ACTIVE',
    created_at       timestamp    not null default current_timestamp,
    updated_at       timestamp    not null default current_timestamp,

    constraint pk_users primary key (id),
    constraint uk_users_normalized_email unique (normalized_email),
    constraint chk_users_status check ( status in ('ACTIVE', 'BLOCKED', 'DELETED'))
);