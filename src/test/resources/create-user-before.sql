delete from user_role;
delete from usr;

insert into usr(id, active, password, username)
    values
    (1, true, '$2a$08$FNcbVLFGXxQzhGl3zLddteplhNEoRQLX1NTEMUfO8xaMnFzeAgJs2', 'admin'),
    (2, true, '$2a$08$FNcbVLFGXxQzhGl3zLddteplhNEoRQLX1NTEMUfO8xaMnFzeAgJs2', 'user');

insert into user_role(user_id, roles)
    VALUES
    (1, 'USER'), (1, 'ADMIN'),
    (2, 'USER');