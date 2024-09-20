package ru.practicum.repository.mapping;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface ResulSetExtractor<T> {
    T extractData(ResultSet rs) throws SQLException;
}
