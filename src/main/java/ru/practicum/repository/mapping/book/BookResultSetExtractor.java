package ru.practicum.repository.mapping.book;

import ru.practicum.model.Author;
import ru.practicum.model.Book;
import ru.practicum.model.Publisher;
import ru.practicum.repository.mapping.ResulSetExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;

import static ru.practicum.repository.utils.ColumnLabels.AUTHOR_ID;
import static ru.practicum.repository.utils.ColumnLabels.AUTHOR_LASTNAME;
import static ru.practicum.repository.utils.ColumnLabels.AUTHOR_NAME;
import static ru.practicum.repository.utils.ColumnLabels.BOOK_ID;
import static ru.practicum.repository.utils.ColumnLabels.BOOK_NAME;
import static ru.practicum.repository.utils.ColumnLabels.BOOK_YEAR;
import static ru.practicum.repository.utils.ColumnLabels.PUBLISHER_CITY;
import static ru.practicum.repository.utils.ColumnLabels.PUBLISHER_ID;
import static ru.practicum.repository.utils.ColumnLabels.PUBLISHER_NAME;

public class BookResultSetExtractor implements ResulSetExtractor<Book> {
    @Override
    public Book extractData(ResultSet rs) throws SQLException {
        Book book = null;
        Author author = null;
        while (rs.next()) {
            Publisher publisher = new Publisher();
            if (author == null) {
                author = Author.builder()
                        .id(rs.getLong(AUTHOR_ID.toString()))
                        .firstName(rs.getString(AUTHOR_NAME.toString()))
                        .lastName(rs.getString(AUTHOR_LASTNAME.toString()))
                        .build();
            }
            if (book == null) {
                book = Book.builder()
                        .id(rs.getLong(BOOK_ID.toString()))
                        .name(rs.getString(BOOK_NAME.toString()))
                        .year(rs.getInt(BOOK_YEAR.toString()))
                        .build();
            }
            if (rs.getLong(PUBLISHER_ID.toString()) != 0) {
                publisher = Publisher.builder()
                        .id(rs.getLong(PUBLISHER_ID.toString()))
                        .name(rs.getString(PUBLISHER_NAME.toString()))
                        .city(rs.getString(PUBLISHER_CITY.toString()))
                        .build();
            }
            book.addPublisher(publisher);
        }
        if (book != null) {
            book.setAuthor(author);
        }
        return book;
    }
}
