package ru.simbir.health.accountservice.features.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import ru.simbir.health.accountservice.features.security.services.activeToken.ActiveTokenService;
import ru.simbir.health.accountservice.features.user.entities.UserEntity;
import ru.simbir.health.accountservice.features.user.entities.role.UserRoleEntityId;
import ru.simbir.health.accountservice.features.user.services.UserService;

import java.security.Key;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    @Value("${security.jwt.access-secret}")
    private String jwtAccessSecret;

    @Value("${security.jwt.refresh-secret}")
    private String jwtRefreshSecret;

    private final ActiveTokenService activeTokenService;
    private final UserService userService;

    private final long accessTokenValidity = 12 * 60 * 60 * 1000; // 12 часов
    private final long refreshTokenValidity = 14 * 24 * 60 * 60 * 1000; // 2 недели

    public String generateAccessToken(final UUID tokenId, final UserEntity user) {
        final var now = new Date();
        final var expiryDate = new Date(now.getTime() + accessTokenValidity);
        final var roles = new ArrayList<UserRoleEntityId.Role>();

        if (user.getRoles() != null) {
            roles.addAll(user.getRoles().stream().map(r -> r.getId().getRole()).toList());
        }

        return Jwts.builder()
                .setSubject(user.getUsername())
                .claim("roles", roles)
                .claim("token_id", tokenId)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(getSigningKey(jwtAccessSecret), SignatureAlgorithm.HS512)
                .compact();
    }

    public String generateRefreshToken(final UUID tokenId, final UserEntity user) {
        final var now = new Date();
        final var expiryDate = new Date(now.getTime() + refreshTokenValidity);

        return Jwts.builder()
                .setSubject(user.getUsername())
                .claim("token_id", tokenId)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(getSigningKey(jwtRefreshSecret), SignatureAlgorithm.HS512)
                .compact();
    }

    public boolean validateAccessToken(final String token) {
        return !isTokenExpired(token, jwtAccessSecret) && activeTokenService.existById(getTokenIdWithAccessToken(token));
    }

    public boolean validateRefreshToken(final String token) {
        return !isTokenExpired(token, jwtRefreshSecret) && activeTokenService.existById(getTokenIdWithRefreshToken(token));
    }

    private boolean isTokenExpired(final String token, final String secret) {
        final Claims claims = Jwts.parserBuilder()
                .setSigningKey(secret)
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.getExpiration().before(new Date());
    }

    private Key getSigningKey(String secret) {
        final byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String getUsernameWithAccessToken(final String token) {
        return getUsername(token, jwtAccessSecret);
    }

    public String getUsernameWithRefreshToken(final String token) {
        return getUsername(token, jwtRefreshSecret);
    }

    private String getUsername(final String token, final String secret) {
        return Jwts
                .parserBuilder()
                .setSigningKey(secret)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public UUID getTokenIdWithAccessToken(final String token) {
        return getTokenId(token, jwtAccessSecret);
    }

    public UUID getTokenIdWithRefreshToken(final String token) {
        return getTokenId(token, jwtRefreshSecret);
    }

    private UUID getTokenId(final String token, final String secret) {
        final var tokenId = Jwts
                .parserBuilder()
                .setSigningKey(secret)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("token_id")
                .toString();

        return UUID.fromString(tokenId);
    }

    public UserDetails getUserDetailsByAccessToken(final String accessToken) {
        final UUID tokenId = getTokenIdWithAccessToken(accessToken);
        final String username = getUsernameWithAccessToken(accessToken);
        return getUserDetailsByUsername(tokenId, username);
    }

    public Authentication getAuthorization(final String accessToken) {
        final UUID tokenId = getTokenIdWithAccessToken(accessToken);
        final String username = getUsernameWithAccessToken(accessToken);
        final UserDetails userDetails = getUserDetailsByUsername(tokenId, username);
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public UserDetails getUserDetailsByUsername(final UUID tokenId, final String username) {
        final UserEntity user = userService.getByUsernameWithRoles(username);
        return JwtUserDetails.create(tokenId, user);
    }
}
