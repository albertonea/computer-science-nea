package com.eastbarnetschool.ordermatchingengine.api.model.entity.rowmapper;

import com.eastbarnetschool.ordermatchingengine.api.model.entity.UserEntity;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.UUID;

public class UserRowMapper implements RowMapper<UserEntity> {
    @Override
    public UserEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new UserEntity(
                rs.getObject("user_id", UUID.class),
                rs.getObject("username", String.class),
                rs.getObject("password", String.class),
                rs.getObject("created_at", Timestamp.class)
        );
    }
}
