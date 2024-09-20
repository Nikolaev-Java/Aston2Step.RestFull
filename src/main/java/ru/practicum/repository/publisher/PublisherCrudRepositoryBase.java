package ru.practicum.repository.publisher;

import ru.practicum.db.ConnectionManager;
import ru.practicum.model.Publisher;
import ru.practicum.repository.BaseCrudRepository;
import ru.practicum.repository.mapping.ResulSetExtractor;
import ru.practicum.repository.mapping.RowMapper;
import ru.practicum.repository.utils.GenereteKeyHolder;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import static ru.practicum.repository.utils.ColumnLabels.AUTHOR_ID;
import static ru.practicum.repository.utils.ColumnLabels.AUTHOR_LASTNAME;
import static ru.practicum.repository.utils.ColumnLabels.AUTHOR_NAME;
import static ru.practicum.repository.utils.ColumnLabels.BOOK_ID;
import static ru.practicum.repository.utils.ColumnLabels.BOOK_NAME;
import static ru.practicum.repository.utils.ColumnLabels.BOOK_YEAR;
import static ru.practicum.repository.utils.ColumnLabels.PUBLISHER_CITY;
import static ru.practicum.repository.utils.ColumnLabels.PUBLISHER_ID;
import static ru.practicum.repository.utils.ColumnLabels.PUBLISHER_NAME;

public class PublisherCrudRepositoryBase extends BaseCrudRepository implements PublisherCrudRepository {
    private final ResulSetExtractor<Publisher> resulSetExtractor;
    private final RowMapper<Publisher> rowMapper;

    public PublisherCrudRepositoryBase(ConnectionManager connectionManager,
                                       ResulSetExtractor<Publisher> resulSetExtractor,
                                       RowMapper<Publisher> rowMapper) {
        super(connectionManager);
        this.resulSetExtractor = resulSetExtractor;
        this.rowMapper = rowMapper;
    }

    @Override
    public Publisher save(Publisher publisher) {
        GenereteKeyHolder keyHolder = new GenereteKeyHolder();
        String sql = "insert into publishers (name,city) values (?,?)";
        Map<Integer, Object> params = getParams(publisher.getName(), publisher.getCity());
        try {
            int update = update(sql, params, keyHolder);

            if (update == 1) {
                if (Objects.nonNull(keyHolder.getKey())) {
                    publisher.setId((long) keyHolder.getKey());
                } else {
                    throw new SQLException("Failed to create publisher");
                }
            }
            return publisher;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Publisher save(Publisher publisher, Long id) {
        String sql = "update publishers set name=?, city=? where id=?";
        Map<Integer, Object> params = getParams(publisher.getName(), publisher.getCity(), id);
        try {
            int update = update(sql, params);
            if (update == 0) {
                throw new SQLException("Failed to update publisher");
            }
            return publisher;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean delete(Long id) {
        String sql = "delete from publishers where id=?";
        Map<Integer, Object> params = getParams(id);
        try {
            int update = update(sql, params);
            return update == 1;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Publisher> findById(Long id) {
        String sql = String.format("""
                               select
                               p.id %s, p.name %s, p.city %s,
                               b.id %s, b.name %s, b.year %s,
                               a.id %s, a.first_name %s, a.last_name %s
                        from publishers p
                                 left join books_publishers bp on p.id = bp.publisher_id
                                 left join books b on b.id = bp.books_id
                                 left join authors a on a.id = b.author_id
                        where p.id=?
                        """, PUBLISHER_ID, PUBLISHER_NAME, PUBLISHER_CITY, BOOK_ID,
                BOOK_NAME, BOOK_YEAR, AUTHOR_ID, AUTHOR_NAME, AUTHOR_LASTNAME);
        try {
            return query(sql, getParams(id), resulSetExtractor);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Publisher> findAll() {
        String sql = "select * from publishers";
        try {
            return queryForList(sql, rowMapper);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
