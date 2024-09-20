package ru.practicum.repository.mapping.author;

import ru.practicum.model.Author;
import ru.practicum.model.Book;
import ru.practicum.model.Publisher;
import ru.practicum.repository.mapping.ResulSetExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static ru.practicum.repository.utils.ColumnLabels.AUTHOR_ID;
import static ru.practicum.repository.utils.ColumnLabels.AUTHOR_LASTNAME;
import static ru.practicum.repository.utils.ColumnLabels.AUTHOR_NAME;
import static ru.practicum.repository.utils.ColumnLabels.BOOK_ID;
import static ru.practicum.repository.utils.ColumnLabels.BOOK_NAME;
import static ru.practicum.repository.utils.ColumnLabels.BOOK_YEAR;
import static ru.practicum.repository.utils.ColumnLabels.PUBLISHER_CITY;
import static ru.practicum.repository.utils.ColumnLabels.PUBLISHER_ID;
import static ru.practicum.repository.utils.ColumnLabels.PUBLISHER_NAME;

public class AuthorResultSetExtractor implements ResulSetExtractor<Author> {
    @Override
    public Author extractData(ResultSet rs) throws SQLException {
        Author author = null;
        List<Book> bookList = new ArrayList<>();
        Book book = new Book();
        while (rs.next()) {
            if (author == null) {
                author = Author.builder()
                        .id(rs.getLong(AUTHOR_ID.toString()))
                        .firstName(rs.getString(AUTHOR_NAME.toString()))
                        .lastName(rs.getString(AUTHOR_LASTNAME.toString()))
                        .build();
            }
            if (rs.getLong(BOOK_ID.toString()) != book.getId()) {
                book = Book.builder()
                        .id(rs.getLong(BOOK_ID.toString()))
                        .name(rs.getString(BOOK_NAME.toString()))
                        .year(rs.getInt(BOOK_YEAR.toString()))
                        .build();
                bookList.add(book);
            }
            if (rs.getLong(PUBLISHER_ID.toString()) != 0) {
                Publisher publisher = Publisher.builder()
                        .id(rs.getLong(PUBLISHER_ID.toString()))
                        .name(rs.getString(PUBLISHER_NAME.toString()))
                        .city(rs.getString(PUBLISHER_CITY.toString()))
                        .build();
                book.addPublisher(publisher);
            }
        }
        if (author != null) {
            author.setBooks(bookList);
        }
        return author;
    }
}
