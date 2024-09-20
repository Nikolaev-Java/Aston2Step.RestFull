package ru.practicum.factory;

import ru.practicum.db.ConnectionManager;
import ru.practicum.db.PosgresConnectionManager;
import ru.practicum.repository.author.AuthorCrudRepositoryBase;
import ru.practicum.repository.book.BookCrudRepositoryBase;
import ru.practicum.repository.mapping.author.AuthorResultSetExtractor;
import ru.practicum.repository.mapping.author.AuthorRowMapper;
import ru.practicum.repository.mapping.book.BookResultSetExtractor;
import ru.practicum.repository.mapping.book.BookRowMapper;
import ru.practicum.repository.mapping.publisher.PublisherResultSetExtractor;
import ru.practicum.repository.mapping.publisher.PublisherRowMapper;
import ru.practicum.repository.publisher.PublisherCrudRepositoryBase;
import ru.practicum.service.author.AuthorService;
import ru.practicum.service.author.AuthorServiceImpl;
import ru.practicum.service.book.BookService;
import ru.practicum.service.book.BookServiceImpl;
import ru.practicum.service.publisher.PublisherService;
import ru.practicum.service.publisher.PublisherServiceImpl;

public class Factory {
    private Factory() {
    }

    private static ConnectionManager getConnectionManager() {
        return new PosgresConnectionManager();
    }

    public static AuthorService getAuthorService() {
        return new AuthorServiceImpl(new AuthorCrudRepositoryBase(getConnectionManager(),
                new AuthorRowMapper(),
                new AuthorResultSetExtractor()));
    }

    public static BookService getBookService() {
        return new BookServiceImpl(new BookCrudRepositoryBase(getConnectionManager(),
                new BookRowMapper(),
                new BookResultSetExtractor()));
    }

    public static PublisherService getPublisherService() {
        return new PublisherServiceImpl(new PublisherCrudRepositoryBase(getConnectionManager(),
                new PublisherResultSetExtractor(),
                new PublisherRowMapper()));
    }
}
