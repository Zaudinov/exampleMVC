delete from message;

insert into message (id, tag, text, user_id)
    VALUES
    (1, 'test', 'First', 1),
    (2, 'test', 'Second', 1),
    (3, 'test', 'Third', 2),
    (4, 'filter test', 'Fourth', 2);

ALTER sequence hibernate_sequence restart with 10;