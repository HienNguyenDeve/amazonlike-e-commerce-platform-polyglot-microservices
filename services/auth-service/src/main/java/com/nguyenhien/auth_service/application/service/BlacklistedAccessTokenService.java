package com.nguyenhien.auth_service.application.service;

import com.nguyenhien.auth_service.application.interfaces.IBlacklistedAccessTokenService;
import com.nguyenhien.auth_service.domain.model.BlacklistedAccessToken;
import com.nguyenhien.auth_service.domain.repository.IBlacklistedAccessTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BlacklistedAccessTokenService implements IBlacklistedAccessTokenService {

  private final IBlacklistedAccessTokenRepository repository;
  private final TokenService tokenService;

  @Override
  public void blacklist(String accessToken) {
    if (accessToken == null || accessToken.isBlank()) {
      return;
    }
    // Check accessToken còn hạn hay không? nếu không trả về
    Long ttl = tokenService.getRemainingExpiration(accessToken);

    if (ttl <= 0) {
      return;
    }

    // Nếu còn thì thêm vào Redis
    BlacklistedAccessToken token =
        BlacklistedAccessToken.builder().token(accessToken).expiration(ttl).build();

    repository.save(token);
  }

  @Override
  public boolean isBlacklisted(String accessToken) {
    return repository.existsById(accessToken);
  }
}
