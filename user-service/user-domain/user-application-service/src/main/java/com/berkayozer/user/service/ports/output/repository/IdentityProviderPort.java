package com.berkayozer.user.service.ports.output.repository;

import com.berkayozer.user.service.domain.entity.User;

import java.util.Date;

public interface IdentityProviderPort {
    String hashPassword(String rawPassword);
    boolean checkPassword(String rawPassword, String encodedPassword);
    String generateAccessToken(User user);
    String generateRefreshToken(User user);

    // Token validation methods for Refresh operations
    boolean validateToken(String token);
    String getEmailFromToken(String token);
    Date getIssueDateFromToken(String token);
    String getRoleFromToken(String token);
}