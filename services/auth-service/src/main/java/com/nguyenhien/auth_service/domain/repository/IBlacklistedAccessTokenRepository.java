package com.nguyenhien.auth_service.domain.repository;

import org.springframework.data.repository.CrudRepository;

import com.nguyenhien.auth_service.domain.model.BlacklistedAccessToken;

public interface IBlacklistedAccessTokenRepository extends CrudRepository<BlacklistedAccessToken, String>{

}
