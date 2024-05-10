package com.nri.ems.Services;

import org.springframework.stereotype.Service;

import com.nri.ems.DTO.RefreshTokenRequest;
import com.nri.ems.Entities.RefreshToken;

@Service
public interface RefreshTokenService {
    RefreshToken createRefreshToken(String username);

    RefreshToken verifyRefreshToken(String token);

    String generateTokenIfExpired(RefreshTokenRequest req);
}
