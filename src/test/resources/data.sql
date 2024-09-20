DELETE
FROM authors;
DELETE
FROM books;
DELETE
FROM publishers;
DELETE
FROM books_publishers;
alter sequence authors_id_seq restart with 1;
alter sequence books_id_seq restart with 1;
alter sequence publishers_id_seq restart with 1;
INSERT INTO authors (first_name, last_name)
VALUES ('name1', 'lastName1'),
       ('name2', 'lastName2'),
       ('name3', 'lastName3');
INSERT INTO books (name, author_id, year)
VALUES ('book1', 1, 1),
       ('book2', 1, 2),
       ('book3', 2, 3),
       ('book4', 3, 4),
       ('book5', 1, 5);
INSERT INTO publishers (name, city)
VALUES ('publisher1', 'city1'),
       ('publisher2', 'city2'),
       ('publisher3', 'city3'),
       ('publisher4', 'city4');
INSERT INTO books_publishers (books_id, publisher_id)
VALUES (1, 1),
       (1, 4),
       (1, 2),
       (2, 3),
       (3, 1),
       (4, 4),
       (5, 3);