package com.berkayozer.user.service.dataaccess.token.adapter;

import com.berkayozer.user.service.ports.output.repository.TokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class TokenRepositoryImpl implements TokenRepository {

    private final StringRedisTemplate redisTemplate;
    private static final long REFRESH_TOKEN_EXPIRATION_DAYS = 7;

    @Override
    public void revokeAllTokensForUser(String email) {
        String key = "user:" + email + ":revoke_date";
        String revokeTimestamp = String.valueOf(System.currentTimeMillis());
        // Assignment 2: Kullanıcının tüm tokenlarını geçersiz kılmak için iptal tarihi atıyoruz
        redisTemplate.opsForValue().set(key, revokeTimestamp, Duration.ofDays(REFRESH_TOKEN_EXPIRATION_DAYS));
    }

    @Override
    public boolean isTokenRevokedForUser(String email, Date issueDate) {
        String key = "user:" + email + ":revoke_date";
        String revokeDateStr = redisTemplate.opsForValue().get(key);

        if (revokeDateStr == null) {
            return false; // İptal tarihi yoksa token geçerlidir
        }

        long revokeDate = Long.parseLong(revokeDateStr);
        // Token, iptal tarihinden ÖNCE üretilmişse, token patlamış demektir (Revoked = true).
        return issueDate.getTime() < revokeDate;
    }
}