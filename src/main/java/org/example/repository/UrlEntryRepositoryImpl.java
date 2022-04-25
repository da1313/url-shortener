package org.example.repository;

import org.example.domain.UrlEntry;
import org.example.repository.mappers.UrlEntryRowMapper;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Repository
public class UrlEntryRepositoryImpl implements UrlEntryRepository {

    private final NamedParameterJdbcTemplate template;
    private final Set<String> columns = Set.of("id", "token", "base_url", "user_id");

    public UrlEntryRepositoryImpl(NamedParameterJdbcTemplate template) {
        this.template = template;
    }

    @Deprecated
    @Override
    public Optional<UrlEntry> findByToken(String token) {
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("token", token);
        try {
            return Optional.ofNullable(
                    template.queryForObject("select u.id as id, u.token as token, u.base_url as base_url, " +
                                    "a.id as user_id, a.email as email, a.password as password, a.is_active as is_active " +
                                    "from url_entry u join app_user a on u.user_id = a.id " +
                                    "where token = :token",
                    parameterSource, new UrlEntryRowMapper())
            );
        } catch (EmptyResultDataAccessException exception){
            return Optional.empty();
        }
    }

    @Override
    public Optional<String> findBaseUrlByToken(String token) {
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("token", token);
        try {
            return Optional.ofNullable(
                    template.queryForObject("select base_url from url_entry where token = :token",
                            parameterSource, ((resultSet, i) -> resultSet.getString("base_url")))
            );
        } catch (EmptyResultDataAccessException exception){
            return Optional.empty();
        }
    }

    @Override
    public UrlEntry save(UrlEntry entry) {
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        parameterSource.addValue("token", entry.getToken());
        parameterSource.addValue("baseUrl", entry.getBaseUrl());
        parameterSource.addValue("userId", entry.getAppUser().getId());
        template.update("insert into url_entry(user_id, token, base_url) values(:userId, :token, :baseUrl)", parameterSource,
                keyHolder, new String[]{"id"});
        Number key = keyHolder.getKey();
        if (key == null){
            throw new DataRetrievalFailureException("Operation didn't return generated id");
        }
        return new UrlEntry(key.longValue(), entry.getAppUser(), entry.getToken(), entry.getBaseUrl());
    }

    @Deprecated
    @Override
    public Optional<UrlEntry> findByUrl(String url) {
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("url", url);
        try {
            return Optional.ofNullable(
                    template.queryForObject("select u.id as id, u.token as token, u.base_url as base_url, " +
                                    "a.id as user_id, a.email as email, a.password as password, a.is_active as is_active " +
                                    "from url_entry u join app_user a on u.user_id = a.id " +
                                    "where base_url = :url",
                            parameterSource, new UrlEntryRowMapper())
            );
        } catch (EmptyResultDataAccessException exception){
            return Optional.empty();
        }
    }

    @Override
    public Optional<Long> checkByBaseUrl(String baseUrl) {
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("url", baseUrl);
        try {
            return Optional.ofNullable(
                    template.queryForObject("select id from url_entry where base_url = :url",
                            parameterSource, ((resultSet, i) -> resultSet.getLong("id")))
            );
        } catch (EmptyResultDataAccessException exception){
            return Optional.empty();
        }
    }

    @Override
    public Optional<UrlEntry> findById(long id) {
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("id", id);
        try {
            return Optional.ofNullable(
                    template.queryForObject("select u.id as id, u.token as token, u.base_url as base_url, " +
                                    "a.id as user_id, a.email as email, a.password as password, a.is_active as is_active " +
                                    "from url_entry u join app_user a on u.user_id = a.id " +
                                    "where u.id = :id",
                            parameterSource, new UrlEntryRowMapper())
            );
        } catch (EmptyResultDataAccessException exception){
            return Optional.empty();
        }
    }

    @Override
    public List<UrlEntry> findAll(int pageNumber, int pageSize, String sort, boolean direction) {
        if (pageNumber < 0 || pageSize < 0){
            throw new IllegalArgumentException("pageNumber or pageSize must not be negative");
        }
        if (!columns.contains(sort)){
            throw new IllegalArgumentException(String.format("Unknown column name: %s", sort));
        }
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        long offset = (long) pageNumber * pageSize;
        parameterSource.addValue("pageSize", pageSize);
        parameterSource.addValue("offset", offset);
        String directionString = direction ? "asc" : "desc";
        return template.query(String.format("select u.id as id, u.token as token, u.base_url as base_url, " +
                "a.id as user_id, a.email as email, a.password as password, a.is_active as is_active " +
                "from url_entry u join app_user a on u.user_id = a.id " +
                "order by %s %s " +
                "limit :pageSize offset :offset", sort, directionString),
                parameterSource, new UrlEntryRowMapper());
    }

    @Override
    public List<UrlEntry> findAllByUser(int pageNumber, int pageSize, String sort, boolean direction, long userId) {
        if (pageNumber < 0 || pageSize < 0){
            throw new IllegalArgumentException("pageNumber or pageSize must not be negative");
        }
        if (!columns.contains(sort)){
            throw new IllegalArgumentException(String.format("Unknown column name: %s", sort));
        }
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        long offset = (long) pageNumber * pageSize;
        parameterSource.addValue("pageSize", pageSize);
        parameterSource.addValue("offset", offset);
        parameterSource.addValue("userId", userId);
        String directionString = direction ? "asc" : "desc";
        return template.query(String.format("select u.id as id, u.token as token, u.base_url as base_url, " +
                        "a.id as user_id, a.email as email, a.password as password, a.is_active as is_active " +
                        "from url_entry u join app_user a on u.user_id = a.id " +
                        "where a.id = :userId " +
                        "order by %s %s " +
                        "limit :pageSize offset :offset", sort, directionString),
                parameterSource, new UrlEntryRowMapper());
    }

    @Override
    public boolean deleteById(long id) {
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("id", id);
        int count = template.update("delete from base_url where id = :id", parameterSource);
        return count > 0;
    }

    @Override
    public UrlEntry update(UrlEntry urlEntry) {
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        parameterSource.addValue("baseUrl", urlEntry.getBaseUrl());
        parameterSource.addValue("token", urlEntry.getToken());
        template.update("update url_entry set token = :token, base_url = :baseUrl " +
                "where id = :id", parameterSource, keyHolder);
        Map<String, Object> keys = keyHolder.getKeys();
//        new UrlEntry(keys.get("id"), null, )
        return urlEntry;
    }

}
