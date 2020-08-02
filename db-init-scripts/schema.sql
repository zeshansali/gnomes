create extension if not exists "uuid-ossp";

set time zone 'UTC';

create table users (
  id          uuid          constraint users_pk primary key default uuid_generate_v4(),
  first_name  varchar(50)   not null check (first_name <> ''),
  last_name   varchar(50)   not null check (last_name <> ''),
  birthday    date          not null,
  email       varchar(50)   unique not null check (email <> ''),
  created_at  timestamp     not null default now(),
  updated_at  timestamp     not null default now()
);

create table auth (
  id          uuid        constraint auth_pk primary key default uuid_generate_v4(),
  usr_id      uuid        not null references users (id),
  usr_name    varchar(20) unique not null check (usr_name <> ''),
  password    text        not null,
  created_at  timestamp   not null default now(),
  updated_at  timestamp   not null default now()
);

-- create table gatherings (

-- )