package ru.practicum.service.book;

import ru.practicum.exception.NotFoundException;
import ru.practicum.model.Book;
import ru.practicum.repository.CrudRepository;
import ru.practicum.service.ServiceUtils;

import java.util.List;

public class BookServiceImpl implements BookService {
    private final CrudRepository<Book, Long> repository;

    public BookServiceImpl(CrudRepository<Book, Long> repository) {
        this.repository = repository;
    }

    @Override
    public List<Book> getAllBooks() {
        return repository.findAll();
    }

    @Override
    public Book getById(long id) {
        return repository.findById(id).orElseThrow(() -> new NotFoundException("Book " + id + " not found"));
    }

    @Override
    public Book create(Book book) {
        return repository.save(book);
    }

    @Override
    public Book update(Book book, long id) {
        Book bookToUpdate = repository.findById(id).orElseThrow(() -> new NotFoundException("Book not found"));
        ServiceUtils.updateEntity(book.getName(), bookToUpdate::setName);
        ServiceUtils.updateEntity(book.getYear(), bookToUpdate::setYear);
        ServiceUtils.updateEntity(book.getAuthor(), bookToUpdate::setAuthor);
        return repository.save(bookToUpdate, id);
    }

    @Override
    public boolean deleteById(long id) {
        return repository.delete(id);
    }
}
