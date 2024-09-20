package ru.practicum.service.author;

import ru.practicum.exception.NotFoundException;
import ru.practicum.model.Author;
import ru.practicum.repository.CrudRepository;
import ru.practicum.service.ServiceUtils;

import java.util.List;

public class AuthorServiceImpl implements AuthorService {
    private final CrudRepository<Author, Long> authorRepository;

    public AuthorServiceImpl(CrudRepository<Author, Long> authorRepository) {
        this.authorRepository = authorRepository;
    }

    @Override
    public Author getById(long id) {
        return authorRepository.findById(id).orElseThrow(() -> new NotFoundException("Author " + id + " not found"));
    }

    @Override
    public List<Author> getAll() {
        return authorRepository.findAll();
    }

    @Override
    public Author create(Author author) {
        return authorRepository.save(author);
    }

    @Override
    public boolean deleteById(long id) {
        return authorRepository.delete(id);
    }

    @Override
    public Author update(Author author, long id) {
        Author authorToUpdate = authorRepository.findById(id).orElseThrow(() -> new RuntimeException("Author not found"));
        ServiceUtils.updateEntity(authorToUpdate, author.getFirstName(),
                authorUpdate -> authorUpdate.setFirstName(author.getFirstName()));
        ServiceUtils.updateEntity(authorToUpdate, author.getLastName(),
                author1 -> author1.setLastName(author.getLastName()));
        return authorRepository.save(authorToUpdate, id);
    }

}
