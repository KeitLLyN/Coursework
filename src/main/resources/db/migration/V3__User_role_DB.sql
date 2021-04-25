create table user_role
(
    user_id bigint not null,
    roles   varchar(255)
) engine = InnoDB;