package org.example.repository;

import org.example.domain.AppUser;
import org.example.repository.mappers.AppUserRowMapper;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class AppUserRepositoryImpl implements AppUserRepository {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public AppUserRepositoryImpl(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    public Optional<AppUser> findByEmail(String email) {
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("email", email);
        try {
            return Optional.ofNullable(namedParameterJdbcTemplate
                    .queryForObject("select id, email, password, is_active " +
                            "from app_user " +
                            "where email = :email", parameterSource, new AppUserRowMapper()));
        } catch (EmptyResultDataAccessException exception){
            return Optional.empty();
        }
    }
}
