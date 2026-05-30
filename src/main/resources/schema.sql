drop table if exists roles;
drop table if exists raw_materials;
drop table if exists persons;

create table persons (
    id bigint auto_increment primary key,
    first_name varchar(100) not null,
    last_name varchar(100) not null,
    email varchar(150) not null unique,
    password varchar(255) not null
);

create table roles (
    id bigint auto_increment primary key,
    role_name varchar(100) not null,
    person_id bigint not null,
    constraint fk_roles_person foreign key (person_id) references persons(id)
);

create table raw_materials (
    id bigint auto_increment primary key,
    code varchar(30) not null unique,
    name varchar(150) not null,
    organic_certified boolean not null,
    price decimal(10,2) not null,
    amount decimal(10,2) not null,
    unit varchar(30) not null
);
