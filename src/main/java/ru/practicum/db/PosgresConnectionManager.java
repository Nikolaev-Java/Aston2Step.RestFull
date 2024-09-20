package ru.practicum.db;

import org.postgresql.ds.PGConnectionPoolDataSource;
import ru.practicum.utils.PropertyUtil;

import java.sql.Connection;
import java.sql.SQLException;

public class PosgresConnectionManager implements ConnectionManager {
    private static final String URL = PropertyUtil.getProperty("postgres.url");
    private static final String USER = PropertyUtil.getProperty("postgres.user");
    private static final String PASSWORD = PropertyUtil.getProperty("postgres.password");

    @Override
    public Connection getConnection() throws SQLException {
        PGConnectionPoolDataSource ds = new PGConnectionPoolDataSource();
        ds.setURL(URL);
        ds.setUser(USER);
        ds.setPassword(PASSWORD);
        return ds.getPooledConnection().getConnection();
    }
}
