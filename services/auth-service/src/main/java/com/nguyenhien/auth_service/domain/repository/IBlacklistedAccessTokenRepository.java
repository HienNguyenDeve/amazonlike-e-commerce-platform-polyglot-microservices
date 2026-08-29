package com.nguyenhien.auth_service.domain.repository;

import com.nguyenhien.auth_service.domain.model.BlacklistedAccessToken;
import org.springframework.data.repository.CrudRepository;

public interface IBlacklistedAccessTokenRepository
    extends CrudRepository<BlacklistedAccessToken, String> {}
