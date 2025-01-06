package com.eastbarnetschool.ordermatchingengine.api.repository;

import com.eastbarnetschool.ordermatchingengine.api.entity.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Repository
public class UserRepositoryImpl implements UserRepository {

    private final NamedParameterJdbcTemplate template;

    public UserRepositoryImpl(NamedParameterJdbcTemplate databaseClient) {
        this.template = databaseClient;
    }

    @Override
    public User findByUsername(String username) {
        return template.queryForObject("select * from users where username = :username",
               Map.of("username", username),
                (rs, rowNum) -> {
            return new User(rs.getObject("user_id", UUID.class), "", Timestamp.from(null));
        });
    }

    @Override
    public User create(String username) {
        template.query("insert into user")
    }
}