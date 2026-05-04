package com.nguyenhien.auth_service.application.interfaces;

import com.nguyenhien.auth_service.api.request.CreateRefreshTokenRequest;
import com.nguyenhien.auth_service.domain.model.RefreshToken;

public interface IRefreshTokenService {
    //Create
    RefreshToken createRefreshToken(CreateRefreshTokenRequest request);

    // Delete
    void delete(String token);

    // Verify
    RefreshToken verifyExpiration(RefreshToken token);
}
