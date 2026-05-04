package com.nguyenhien.auth_service.domain.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

import com.nguyenhien.auth_service.domain.model.RefreshToken;

public interface IRefreshTokenRepository extends CrudRepository<RefreshToken, String> {
    Optional<RefreshToken> findByToken(String token);

    List<RefreshToken> findAllByUserId(UUID userId);

    void deleteByUserId(UUID userId); // Dùng khi muốn logout mọi thiết bị
}
