package ru.practicum.repository.author;

import ru.practicum.db.ConnectionManager;
import ru.practicum.model.Author;
import ru.practicum.repository.BaseCrudRepository;
import ru.practicum.repository.mapping.ResulSetExtractor;
import ru.practicum.repository.mapping.RowMapper;
import ru.practicum.repository.utils.ColumnLabels;
import ru.practicum.repository.utils.GenereteKeyHolder;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class AuthorCrudRepositoryBase extends BaseCrudRepository implements AuthorCrudRepository {
    private final RowMapper<Author> rowMapper;
    private final ResulSetExtractor<Author> resulSetExtractor;

    public AuthorCrudRepositoryBase(ConnectionManager connectionManager,
                                    RowMapper<Author> rowMapper,
                                    ResulSetExtractor<Author> resulSetExtractor) {
        super(connectionManager);
        this.rowMapper = rowMapper;
        this.resulSetExtractor = resulSetExtractor;
    }

    @Override
    public Author save(Author author) {
        GenereteKeyHolder keyHolder = new GenereteKeyHolder();
        String sql = "insert into authors (first_name, last_name) values (?, ?)";
        Map<Integer, Object> params = getParams(author.getFirstName(), author.getLastName());
        try {
            int update = update(sql, params, keyHolder);
            if (update == 1) {
                if (Objects.nonNull(keyHolder.getKey())) {
                    author.setId((Long) keyHolder.getKey());
                } else {
                    throw new SQLException("Failed to create author");
                }
            }
            return author;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Author save(Author author, Long id) {
        String sql = "update authors set first_name = ?, last_name = ? where id = ?";
        Map<Integer, Object> params = getParams(author.getFirstName(), author.getLastName(), id);
        try {
            int update = update(sql, params);
            if (update != 1) {
                throw new SQLException("Failed to update author");
            }
            return author;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean delete(Long id) {
        String sql = "delete from authors where id = ?";
        Map<Integer, Object> params = getParams(id);
        try {
            int update = update(sql, params);
            return update == 1;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Author> findById(Long id) {
        String sql = String.format("""
                        select a.id %s, a.first_name %s, a.last_name %s,
                        b.id %s, b.name %s, b.year %s, p.id %s, p.name %s, p.city %s
                        from authors a
                        left join books b on a.id = b.author_id
                        left join books_publishers bp on b.id = bp.books_id
                        left join publishers p on p.id = bp.publisher_id
                        where a.id = ?
                        """, ColumnLabels.AUTHOR_ID, ColumnLabels.AUTHOR_NAME, ColumnLabels.AUTHOR_LASTNAME,
                ColumnLabels.BOOK_ID, ColumnLabels.BOOK_NAME, ColumnLabels.BOOK_YEAR,
                ColumnLabels.PUBLISHER_ID, ColumnLabels.PUBLISHER_NAME, ColumnLabels.PUBLISHER_CITY);
        Map<Integer, Object> params = getParams(id);
        try {
            return query(sql, params, resulSetExtractor);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Author> findAll() {
        String sql = """
                Select * from authors
                """;
        try {
            return queryForList(sql, rowMapper);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


}
