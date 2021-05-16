create table message
(
    id      bigint        not null auto_increment,
    tag     varchar(255),
    text    varchar(2048) not null,
    user_id bigint,
    primary key (id)
) engine = InnoDB;