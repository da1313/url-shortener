package org.example.repository.mappers;

import org.example.domain.AppUser;
import org.example.domain.UrlEntry;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UrlEntryRowMapper implements RowMapper<UrlEntry> {
    @Override
    public UrlEntry mapRow(ResultSet resultSet, int i) throws SQLException {
        return new UrlEntry(
                resultSet.getLong("id"),
                new AppUser(
                        resultSet.getLong("user_id"),
                        resultSet.getString("email"),
                        resultSet.getString("password"),
                        resultSet.getBoolean("is_active")
                ),
                resultSet.getString("token"),
                resultSet.getString("base_url"));
    }
}
