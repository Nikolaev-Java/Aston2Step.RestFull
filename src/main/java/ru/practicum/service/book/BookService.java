package ru.practicum.service.book;

import ru.practicum.model.Book;

import java.util.List;

public interface BookService {
    List<Book> getAllBooks();

    Book getById(long id);

    Book create(Book book);

    Book update(Book book, long id);

    boolean deleteById(long id);
}
