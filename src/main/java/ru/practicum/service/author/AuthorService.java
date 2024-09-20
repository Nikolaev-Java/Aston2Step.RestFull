package ru.practicum.service.author;

import ru.practicum.model.Author;

import java.util.List;

public interface AuthorService {
    Author getById(long id);

    List<Author> getAll();

    Author create(Author author);

    boolean deleteById(long id);

    Author update(Author author, long id);
}
