package ru.practicum.repository.mapping.publisher;

import ru.practicum.model.Author;
import ru.practicum.model.Book;
import ru.practicum.model.Publisher;
import ru.practicum.repository.mapping.ResulSetExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static ru.practicum.repository.utils.ColumnLabels.AUTHOR_ID;
import static ru.practicum.repository.utils.ColumnLabels.AUTHOR_NAME;
import static ru.practicum.repository.utils.ColumnLabels.BOOK_ID;
import static ru.practicum.repository.utils.ColumnLabels.BOOK_NAME;
import static ru.practicum.repository.utils.ColumnLabels.BOOK_YEAR;
import static ru.practicum.repository.utils.ColumnLabels.PUBLISHER_CITY;
import static ru.practicum.repository.utils.ColumnLabels.PUBLISHER_ID;
import static ru.practicum.repository.utils.ColumnLabels.PUBLISHER_NAME;

public class PublisherResultSetExtractor implements ResulSetExtractor<Publisher> {
    @Override
    public Publisher extractData(ResultSet rs) throws SQLException {
        Publisher publisher = null;
        Author author = new Author();
        List<Book> books = new ArrayList<>();
        while (rs.next()) {
            Book book = new Book();
            if (publisher == null) {
                publisher = Publisher.builder()
                        .id(rs.getLong(PUBLISHER_ID.toString()))
                        .name(rs.getString(PUBLISHER_NAME.toString()))
                        .city(rs.getString(PUBLISHER_CITY.toString()))
                        .build();
            }
            if (rs.getLong(BOOK_ID.toString()) != book.getId()) {
                book = Book.builder()
                        .id(rs.getLong(BOOK_ID.toString()))
                        .name(rs.getString(BOOK_NAME.toString()))
                        .author(author)
                        .year(rs.getInt(BOOK_YEAR.toString()))
                        .build();
                books.add(book);
            }
            if (rs.getLong(AUTHOR_ID.toString()) != author.getId()) {
                author = Author.builder()
                        .id(rs.getLong(AUTHOR_ID.toString()))
                        .firstName(rs.getString(AUTHOR_NAME.toString()))
                        .lastName(rs.getString(AUTHOR_NAME.toString()))
                        .build();
                book.setAuthor(author);
            }
        }
        if (publisher != null) {
            publisher.setBooks(books);
        }
        return publisher;
    }
}
