package com.berkayozer.user.service.ports.output.repository;

import java.util.Date;

public interface TokenRepository { //for Redis Blacklist
    // Assignment 2'deki "user:email:revoke_date" redis logic'i için
    void revokeAllTokensForUser(String email);
    boolean isTokenRevokedForUser(String email, Date issueDate);
}