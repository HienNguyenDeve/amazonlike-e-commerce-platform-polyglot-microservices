package com.nguyenhien.auth_service.application.interfaces;

public interface IBlacklistedAccessTokenService {
    void blacklist(String accessToken);
    boolean isBlacklisted(String accessToken);
}
