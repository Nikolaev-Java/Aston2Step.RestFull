package ru.practicum.repository;

import org.postgresql.ds.PGConnectionPoolDataSource;
import ru.practicum.db.ConnectionManager;

import java.sql.Connection;
import java.sql.SQLException;

public class TestPostgresConnection implements ConnectionManager {
    private final String url;
    private final String user;
    private final String password;

    public TestPostgresConnection(String url, String user, String password) {
        this.url = url;
        this.user = user;
        this.password = password;
    }

    @Override
    public Connection getConnection() throws SQLException {
        PGConnectionPoolDataSource ds = new PGConnectionPoolDataSource();
        ds.setURL(url);
        ds.setUser(user);
        ds.setPassword(password);
        return ds.getPooledConnection().getConnection();
    }
}
