DROP TABLE IF EXISTS authors, books, books_publishers, publishers CASCADE;
CREATE TABLE IF NOT EXISTS authors
(
    id         BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    first_name VARCHAR(150)                            NOT NULL,
    last_name  VARCHAR(150)                            NOT NULL,
    CONSTRAINT pk_authors PRIMARY KEY (id)
);
CREATE TABLE IF NOT EXISTS publishers
(
    id   BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    name VARCHAR(255)                            NOT NULL,
    city VARCHAR(100)                            NOT NULL,
    CONSTRAINT pk_publishers PRIMARY KEY (id)
);
CREATE TABLE IF NOT EXISTS books
(
    id        BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    name      VARCHAR(255)                            NOT NULL,
    author_id BIGINT                                  NOT NULL,
    year      INTEGER                                 NOT NULL,
    CONSTRAINT pk_books PRIMARY KEY (id),
    CONSTRAINT fk_author_id_authors FOREIGN KEY (author_id) REFERENCES authors ON DELETE CASCADE
);
CREATE TABLE IF NOT EXISTS books_publishers
(
    books_id     BIGINT NOT NULL,
    publisher_id BIGINT NOT NULL,
    CONSTRAINT fk_books_id_books FOREIGN KEY (books_id) REFERENCES books ON DELETE CASCADE,
    CONSTRAINT fk_publisher_id_publishers FOREIGN KEY (publisher_id) REFERENCES publishers ON DELETE CASCADE
);