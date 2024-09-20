package ru.practicum.repository;

import ru.practicum.db.ConnectionManager;
import ru.practicum.repository.mapping.ResulSetExtractor;
import ru.practicum.repository.mapping.RowMapper;
import ru.practicum.repository.utils.KeyHolder;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

public abstract class BaseCrudRepository {
    protected final ConnectionManager connectionManager;

    protected BaseCrudRepository(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

    protected void setParameters(PreparedStatement ps, Map<Integer, Object> parameters) throws SQLException {
        if (parameters != null)
            for (Integer i : parameters.keySet()) {
                ps.setObject(i, parameters.get(i));
            }
    }

    protected <T> Optional<T> query(String query, Map<Integer, Object> parameters, RowMapper<T> rowMapper) throws SQLException {
        try (PreparedStatement ps = connectionManager.getConnection().prepareStatement(query)) {
            setParameters(ps, parameters);
            ResultSet resultSet = ps.executeQuery();
            if (resultSet.next()) {
                return Optional.of(rowMapper.mapRow(resultSet));
            } else {
                return Optional.empty();
            }
        }
    }

    protected <T> Optional<T> query(String query, Map<Integer, Object> parameters, ResulSetExtractor<T> resulSetExtractor) throws SQLException {
        try (PreparedStatement ps = connectionManager.getConnection().prepareStatement(query)) {
            setParameters(ps, parameters);
            ResultSet resultSet = ps.executeQuery();
            return Optional.ofNullable(resulSetExtractor.extractData(resultSet));
        }
    }

    protected <T> List<T> queryForList(String query, Map<Integer, Object> parameters, RowMapper<T> rowMapper) throws SQLException {
        List<T> results = new ArrayList<>();
        try (PreparedStatement ps = connectionManager.getConnection().prepareStatement(query)) {
            setParameters(ps, parameters);
            ResultSet resultSet = ps.executeQuery();
            while (resultSet.next()) {
                results.add(rowMapper.mapRow(resultSet));
            }
            return results;
        }
    }

    protected <T> List<T> queryForList(String query, RowMapper<T> rowMapper) throws SQLException {
        return queryForList(query, null, rowMapper);
    }

    protected <T> List<T> queryForList(String query, Map<Integer, Object> parameters, ResulSetExtractor<T> resulSetExtractor) throws SQLException {
        List<T> results = new ArrayList<>();
        try (PreparedStatement ps = connectionManager.getConnection().prepareStatement(query)) {
            setParameters(ps, parameters);
            ResultSet resultSet = ps.executeQuery();
            while (resultSet.next()) {
                results.add(resulSetExtractor.extractData(resultSet));
            }
            return results;
        }
    }

    protected int update(String query, Map<Integer, Object> parameters) throws SQLException {
        try (PreparedStatement ps = connectionManager.getConnection().prepareStatement(query)) {
            setParameters(ps, parameters);
            return ps.executeUpdate();
        }
    }

    protected int update(String query, Map<Integer, Object> parameters, KeyHolder keyHolder) throws SQLException {
        try (PreparedStatement ps = connectionManager.getConnection().prepareStatement(query, RETURN_GENERATED_KEYS)) {
            setParameters(ps, parameters);
            int executeUpdate = ps.executeUpdate();
            ResultSet generatedKeys = ps.getGeneratedKeys();
            if (generatedKeys.next()) {
                keyHolder.setKey(generatedKeys.getLong(1));
            }
            return executeUpdate;
        }
    }

    protected Map<Integer, Object> getParams(Object... objects) {
        Map<Integer, Object> params = new HashMap<>();
        for (int i = 0; i < objects.length; i++) {
            params.put(i + 1, objects[i]);
        }
        return params;
    }
}
