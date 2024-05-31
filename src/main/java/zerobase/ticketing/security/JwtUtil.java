package zerobase.ticketing.security;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;


import io.jsonwebtoken.Claims;
import zerobase.ticketing.domain.Booking;
import zerobase.ticketing.domain.User;
import zerobase.ticketing.repository.BlackListRepository;
import zerobase.ticketing.service.CustomUserDetailsService;

import java.time.ZonedDateTime;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtUtil {

    @Value("${jwt.secret.key}")
    private String secretKey;

    private long accessTokenExpTime = 1000 * 60 * 60 * 7;;

    private final CustomUserDetailsService customUserDetailsService;
    private final BlackListRepository blackListRepository;

    // 토큰 생성
    public String createToken(User user) {
        Claims claims = Jwts.claims();
        claims.put("userId", user.getUserId());
        claims.put("role", user.getRoles());

        ZonedDateTime now = ZonedDateTime.now();
        ZonedDateTime expiredDate = now.plusSeconds(accessTokenExpTime);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(Date.from(now.toInstant()))
                .setExpiration(Date.from(expiredDate.toInstant()))
                .signWith(SignatureAlgorithm.HS256, this.secretKey)
                .compact();

    }

    public boolean validateToken(String token) {

        try {
            Jwts.parserBuilder().setSigningKey(this.secretKey).build().parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException e) {
            return false;
        }
    }

    public Claims parseClaims(String token) {

        try {
            return Jwts.parserBuilder().setSigningKey(this.secretKey).build().parseClaimsJws(token).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

    public String getUserId(String token) {
        return parseClaims(token).get("userId", String.class);
    }

    public Authentication getAuthentication(String token) {
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(this.getUserId(token));

        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

    public Boolean isBlackToken(String token) {
        return blackListRepository.existsByInvalidToken(token);
    }
}
