package com.lookatbar.scp.basicdata.infrastructure.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * JWT工具类
 * 提供JWT令牌的生成、解析和验证功能
 */
@Component
public class JwtUtil {

    /**
     * JWT密钥（必须至少256位）
     */
    @Value("${jwt.secret:scp-basicdata-jwt-secret-key-256-bit-minimum-length}")
    private String secret;

    /**
     * JWT过期时间（分钟）
     */
    @Value("${jwt.expire-minutes:120}")
    private long expireMinutes;

    /**
     * 生成JWT令牌
     *
     * @param userId   用户ID
     * @param username 用户名
     * @return JWT令牌字符串
     */
    public String generateToken(String userId, String username) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        claims.put("username", username);
        
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expireTime = now.plusMinutes(expireMinutes);
        
        return Jwts.builder()
                .claims(claims)
                .issuedAt(Date.from(now.atZone(ZoneId.systemDefault()).toInstant()))
                .expiration(Date.from(expireTime.atZone(ZoneId.systemDefault()).toInstant()))
                .signWith(getSigningKey())
                .compact();
    }

    /**
     * 生成JWT令牌（带额外声明）
     *
     * @param userId   用户ID
     * @param username 用户名
     * @param extraClaims 额外声明
     * @return JWT令牌字符串
     */
    public String generateToken(String userId, String username, Map<String, Object> extraClaims) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        claims.put("username", username);
        if (extraClaims != null) {
            claims.putAll(extraClaims);
        }
        
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expireTime = now.plusMinutes(expireMinutes);
        
        return Jwts.builder()
                .claims(claims)
                .issuedAt(Date.from(now.atZone(ZoneId.systemDefault()).toInstant()))
                .expiration(Date.from(expireTime.atZone(ZoneId.systemDefault()).toInstant()))
                .signWith(getSigningKey())
                .compact();
    }

    /**
     * 解析JWT令牌，获取所有声明
     *
     * @param token JWT令牌
     * @return 声明对象
     */
    public Claims parseToken(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /**
     * 从JWT令牌中提取用户ID
     *
     * @param token JWT令牌
     * @return 用户ID
     */
    public String getUserIdFromToken(String token) {
        Claims claims = parseToken(token);
        return claims.get("userId", String.class);
    }

    /**
     * 从JWT令牌中提取用户名
     *
     * @param token JWT令牌
     * @return 用户名
     */
    public String getUsernameFromToken(String token) {
        Claims claims = parseToken(token);
        return claims.get("username", String.class);
    }

    /**
     * 验证JWT令牌是否有效
     *
     * @param token JWT令牌
     * @return 是否有效
     */
    public boolean validateToken(String token) {
        try {
            parseToken(token);
            return !isTokenExpired(token);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 检查JWT令牌是否过期
     *
     * @param token JWT令牌
     * @return 是否已过期
     */
    public boolean isTokenExpired(String token) {
        Claims claims = parseToken(token);
        Date expiration = claims.getExpiration();
        return expiration.before(new Date());
    }

    /**
     * 获取令牌过期时间
     *
     * @param token JWT令牌
     * @return 过期时间
     */
    public LocalDateTime getExpirationTime(String token) {
        Claims claims = parseToken(token);
        Date expiration = claims.getExpiration();
        return expiration.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    /**
     * 获取过期时间（秒）
     *
     * @return 过期时间（秒）
     */
    public long getExpireSeconds() {
        return expireMinutes * 60;
    }

    /**
     * 获取签名密钥
     *
     * @return 签名密钥
     */
    private SecretKey getSigningKey() {
        byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
