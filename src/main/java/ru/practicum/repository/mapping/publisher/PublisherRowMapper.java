package ru.practicum.repository.mapping.publisher;

import ru.practicum.model.Publisher;
import ru.practicum.repository.mapping.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PublisherRowMapper implements RowMapper<Publisher> {
    @Override
    public Publisher mapRow(ResultSet rs) throws SQLException {
        return Publisher.builder()
                .id(rs.getLong("id"))
                .name(rs.getString("name"))
                .city(rs.getString("city"))
                .build();
    }
}
