package com.chanyongyang.hello.security;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import com.chanyongyang.hello.model.UserEntity;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import javafx.application.Application;

@Service

public class TokenProvider {
  private static final String SECRET_KEY = "1234";

  // token 생성
  public String create(UserEntity userEntity) {
    // 기한은 지금으로부터 1일로 설정
    Date expiryDate = Date.from(
        Instant.now().plus(1, ChronoUnit.DAYS));
    return Jwts.builder()
        .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
        .setSubject(userEntity.getId()) // key를 생성한 사람의 '제목'이라는 형태로 사용
        .setIssuer("myApp") // 만든사람
        .setIssuedAt(new Date()) // 언제 만들었는지
        .setExpiration(expiryDate)
        .compact();
  }

  public String create(Authentication authentication) {
    // 기한은 지금으로부터 1일로 설정
    Date expiryDate = Date.from(
        Instant.now().plus(1, ChronoUnit.DAYS));
    return Jwts.builder()
        .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
        .setSubject(((ApplicationOAuth2User) authentication.getPrincipal()).getName())
        .setIssuer("myApp") // 만든사람
        .setIssuedAt(new Date()) // 언제 만들었는지
        .setExpiration(expiryDate)
        .compact();
  }

  // token id 반환
  public String validateAndGetUserId(String token) {
    Claims claims = Jwts.parser()
        .setSigningKey(SECRET_KEY) // 복호화 작업
        .parseClaimsJws(token) // 복호화 하여 userid를 알아냄
        .getBody();
    return claims.getSubject();
  }
}
