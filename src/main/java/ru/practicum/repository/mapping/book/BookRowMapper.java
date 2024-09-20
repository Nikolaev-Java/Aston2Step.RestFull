package ru.practicum.repository.mapping.book;

import ru.practicum.model.Author;
import ru.practicum.model.Book;

import java.sql.ResultSet;
import java.sql.SQLException;

import static ru.practicum.repository.utils.ColumnLabels.AUTHOR_ID;
import static ru.practicum.repository.utils.ColumnLabels.AUTHOR_NAME;
import static ru.practicum.repository.utils.ColumnLabels.BOOK_ID;
import static ru.practicum.repository.utils.ColumnLabels.BOOK_NAME;
import static ru.practicum.repository.utils.ColumnLabels.BOOK_YEAR;

public class BookRowMapper implements ru.practicum.repository.mapping.RowMapper<Book> {
    @Override
    public Book mapRow(ResultSet rs) throws SQLException {
        Author author = Author.builder()
                .id(rs.getLong(AUTHOR_ID.toString()))
                .firstName(rs.getString(AUTHOR_NAME.toString()))
                .lastName(rs.getString(AUTHOR_NAME.toString()))
                .build();

        return Book.builder()
                .id(rs.getLong(BOOK_ID.toString()))
                .name(rs.getString(BOOK_NAME.toString()))
                .author(author)
                .year(rs.getInt(BOOK_YEAR.toString()))
                .build();
    }
}
