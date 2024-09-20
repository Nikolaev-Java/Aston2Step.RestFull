package ru.practicum.repository.book;

import ru.practicum.model.Book;
import ru.practicum.repository.CrudRepository;

public interface BookCrudRepository extends CrudRepository<Book, Long> {
}
