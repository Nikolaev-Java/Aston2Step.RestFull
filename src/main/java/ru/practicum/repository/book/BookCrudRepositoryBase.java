package ru.practicum.repository.book;

import ru.practicum.db.ConnectionManager;
import ru.practicum.model.Book;
import ru.practicum.model.Publisher;
import ru.practicum.repository.BaseCrudRepository;
import ru.practicum.repository.mapping.ResulSetExtractor;
import ru.practicum.repository.mapping.RowMapper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static ru.practicum.repository.utils.ColumnLabels.AUTHOR_ID;
import static ru.practicum.repository.utils.ColumnLabels.AUTHOR_LASTNAME;
import static ru.practicum.repository.utils.ColumnLabels.AUTHOR_NAME;
import static ru.practicum.repository.utils.ColumnLabels.BOOK_ID;
import static ru.practicum.repository.utils.ColumnLabels.BOOK_NAME;
import static ru.practicum.repository.utils.ColumnLabels.BOOK_YEAR;
import static ru.practicum.repository.utils.ColumnLabels.PUBLISHER_CITY;
import static ru.practicum.repository.utils.ColumnLabels.PUBLISHER_ID;
import static ru.practicum.repository.utils.ColumnLabels.PUBLISHER_NAME;

public class BookCrudRepositoryBase extends BaseCrudRepository implements BookCrudRepository {
    private final RowMapper<Book> rowMapper;
    private final ResulSetExtractor<Book> resultSetExtractor;

    public BookCrudRepositoryBase(ConnectionManager connectionManager,
                                  RowMapper<Book> rowMapper,
                                  ResulSetExtractor<Book> resultSetExtractor) {
        super(connectionManager);
        this.rowMapper = rowMapper;
        this.resultSetExtractor = resultSetExtractor;
    }

    @Override
    public Book save(Book book) {
        String sql = "insert into books (name, author_id, year) values (?, ?, ?)";
        String sqlPublishers = "insert into books_publishers (books_id, publisher_id) values (?,?)";
        Map<Integer, Object> params = getParams(book.getName(), book.getAuthor().getId(), book.getYear());
        List<Long> publishersIds = book.getPublishers().stream().map(Publisher::getId).toList();
        Connection conn = null;
        try {
            conn = connectionManager.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        try (PreparedStatement ps = conn.prepareStatement(sql, RETURN_GENERATED_KEYS);
             PreparedStatement addPublishers = conn.prepareStatement(sqlPublishers)) {
            conn.setAutoCommit(false);
            setParameters(ps, params);
            ps.executeUpdate();
            ResultSet generatedKeys = ps.getGeneratedKeys();
            if (generatedKeys.next()) {
                book.setId(generatedKeys.getLong(1));
            }
            for (Long publishersId : publishersIds) {
                addPublishers.setLong(1, book.getId());
                addPublishers.setLong(2, publishersId);
                addPublishers.addBatch();
            }
            addPublishers.executeBatch();
            conn.commit();
            return book;
        } catch (SQLException e) {
            try {
                conn.rollback();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            throw new RuntimeException(e);
        }
    }

    @Override
    public Book save(Book book, Long id) {
        String sql = "update books set name = ?, author_id = ?, year = ? where id = ?";
        Map<Integer, Object> params = getParams(book.getName(), book.getAuthor().getId(), book.getYear(), id);
        try {
            int update = update(sql, params);
            if (update == 0) {
                throw new SQLException("Failed to update book");
            }
            return book;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean delete(Long id) {
        String sql = "delete from books where id = ?";
        Map<Integer, Object> params = getParams(id);
        try {
            int update = update(sql, params);
            return update == 1;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Book> findById(Long id) {
        String sql = String.format("""
                                 select b.id %s, b.name %s, b.year %s, a.id %s, a.first_name %s,
                        a.last_name %s, p.id %s, p.name %s, p.city %s  from books b
                                 left join authors a on b.author_id = a.id
                                 left join books_publishers pb on b.id = pb.books_id
                                 left join public.publishers p on pb.publisher_id = p.id
                                 where b.id = ?
                        """, BOOK_ID, BOOK_NAME, BOOK_YEAR, AUTHOR_ID, AUTHOR_NAME,
                AUTHOR_LASTNAME, PUBLISHER_ID, PUBLISHER_NAME, PUBLISHER_CITY);
        Map<Integer, Object> params = getParams(id);
        try {
            return query(sql, params, resultSetExtractor);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Book> findAll() {
        String sql = String.format("select b.id %s, b.name %s, b.year %s, a.id %s, a.first_name %s, " +
                                   "a.last_name %s from books b join authors a on a.id = b.author_id",
                BOOK_ID, BOOK_NAME, BOOK_YEAR, AUTHOR_ID, AUTHOR_NAME,
                AUTHOR_LASTNAME);
        try {
            return queryForList(sql, rowMapper);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
