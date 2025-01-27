package com.eastbarnetschool.ordermatchingengine.api.model.entity.rowmapper;

import com.eastbarnetschool.ordermatchingengine.api.model.entity.RefreshTokenEntity;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.UUID;

public class RefreshTokenRowMapper implements RowMapper<RefreshTokenEntity> {
    @Override
    public RefreshTokenEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new RefreshTokenEntity(
                rs.getObject("id", UUID.class),
                rs.getObject("user_id", UUID.class),
                rs.getObject("created_at", Timestamp.class),
                rs.getObject("expires_at", Timestamp.class)
        );
    }
}
