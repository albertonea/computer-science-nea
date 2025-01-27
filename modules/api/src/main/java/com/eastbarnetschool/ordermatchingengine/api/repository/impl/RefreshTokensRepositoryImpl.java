package com.eastbarnetschool.ordermatchingengine.api.repository.impl;

import com.eastbarnetschool.ordermatchingengine.api.model.entity.RefreshTokenEntity;
import com.eastbarnetschool.ordermatchingengine.api.model.entity.rowmapper.RefreshTokenRowMapper;
import com.eastbarnetschool.ordermatchingengine.api.repository.RefreshTokensRepository;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Repository
public class RefreshTokensRepositoryImpl implements RefreshTokensRepository {
    private final NamedParameterJdbcTemplate template;

    public RefreshTokensRepositoryImpl(NamedParameterJdbcTemplate template) {
        this.template = template;
    }

    @Override
    public void save(RefreshTokenEntity refreshTokenEntity) {
        template.update(
                """
                insert into refresh_tokens(id, user_id, expires_at)
                values(:id, :userId, :expiresAt)
                """,
                Map.of(
                    "id", refreshTokenEntity.getId(),
                    "userId", refreshTokenEntity.getUserId(),
                    "expiresAt", refreshTokenEntity.getExpiresAt()
                )
        );
    }

    @Override
    public Optional<RefreshTokenEntity> findByIdAndUnexpired(UUID refreshToken) {
        return Optional.ofNullable(template.queryForObject(
                """
                        select * from refresh_tokens where id = :refreshToken and expires_at > now()
                        """,
                Map.of("refreshToken", refreshToken),
                new RefreshTokenRowMapper()
        ));
    }

    @Override
    public void deleteById(UUID refreshToken) {
        template.update(
                """
                delete from refresh_tokens where id = :refreshToken
                """,
                Map.of("refreshToken", refreshToken)
        );
    }
}
