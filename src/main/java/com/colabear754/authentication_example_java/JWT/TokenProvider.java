package com.colabear754.authentication_example_java.JWT;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.stereotype.Service;

import javax.crypto.spec.SecretKeySpec;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@PropertySource("classpath:jwt.yml")
@Service
public class TokenProvider {
    private final String secretKey;
    private final long expirationHours;
    private final String issuer;


    public TokenProvider(
            @Value("${secret-key}") String secretKey,
            @Value("${expiration-hours}") long expirationHours,
            @Value("${issuer}") String issuer
    ) {
        this.secretKey = secretKey;
        this.expirationHours = expirationHours;
        this.issuer = issuer;
    }

    public String createToken(String userSpecification) {
        return Jwts.builder()
                .signWith(new SecretKeySpec(secretKey.getBytes(), SignatureAlgorithm.HS512.getJcaName())) // note HS512 알고리즘을 사용하여  SecretKey를 이용해 서명
                .setSubject(userSpecification) //note JWT 토큰 제목 (보통 user의 아이디로 지정)
                .setIssuer(issuer) // note JWT 토큰 발급자
                .setIssuedAt(Timestamp.valueOf(LocalDateTime.now())) // note JWT 토큰 발급 시간
                .setExpiration(Date.from(Instant.now().plus(expirationHours, ChronoUnit.HOURS))) // note JWT 만료 시간
                .compact(); // note JWT 생성
    }


    public String validateTokenAndGetSubject(String token) { // note 저장되어 있는 토큰을 가져온다.
        return Jwts.parserBuilder()
                .setSigningKey(secretKey.getBytes()) // note JWT token SecretKey
                .build()
                .parseClaimsJws(token) // note 파라미터로 받은 Token
                .getBody()
                .getSubject(); // note Subject()에 저장되어있는 아이디를 꺼내온다
    }

}
