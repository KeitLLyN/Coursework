alter table user_role
    add constraint user_role_user_fk foreign key (user_id) references users (id);