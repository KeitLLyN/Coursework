alter table message
    add constraint message_user_fk foreign key (user_id) references users (id);