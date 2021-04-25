create table users
(
    id              bigint       not null,
    email           varchar(255),
    password        varchar(255) not null,
    username        varchar(255) not null,
    primary key (id)
) engine = InnoDB;