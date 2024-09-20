package ru.practicum.repository.mapping.author;

import ru.practicum.model.Author;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AuthorRowMapper implements ru.practicum.repository.mapping.RowMapper<Author> {
    @Override
    public Author mapRow(ResultSet rs) throws SQLException {
        return Author.builder()
                .id(rs.getLong("id"))
                .lastName(rs.getString("last_name"))
                .firstName(rs.getString("first_name"))
                .build();

    }
}
