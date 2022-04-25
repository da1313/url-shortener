--liquibase formatted sql
--changeset belfanio:1
create table app_user(
    id bigserial primary key,
    email varchar(100) not null,
    password varchar(100) not null,
    is_active boolean not null
);

create table url_entry(
    id bigserial primary key,
    user_id bigint not null references app_user(id),
    token varchar(12) unique not null,
    base_url text not null
);