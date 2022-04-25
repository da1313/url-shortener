--liquibase formatted sql
--changeset belfanio:2
insert into app_user(email, password, is_active) values('user_1@mail.com', '$2a$10$3loEsSUqKOqqPpmgWS5pDe90Gru1K3/TDee7PxibocRwKOuAtlhI6', true);
insert into app_user(email, password, is_active) values('user_2@mail.com', '$2a$10$3loEsSUqKOqqPpmgWS5pDe90Gru1K3/TDee7PxibocRwKOuAtlhI6', true);

insert into url_entry(user_id, base_url, token) values(1, 'http://test-url.com', 'token');
insert into url_entry(user_id, base_url, token) values(1, 'http://test-url-1.com', 'token-1');
insert into url_entry(user_id, base_url, token) values(1, 'http://test-url-2.com', 'token-2');
insert into url_entry(user_id, base_url, token) values(2, 'http://test-url-3.com', 'token-3');
insert into url_entry(user_id, base_url, token) values(2, 'http://test-url-4.com', 'token-4');
insert into url_entry(user_id, base_url, token) values(2, 'http://test-url-5.com', 'token-5');