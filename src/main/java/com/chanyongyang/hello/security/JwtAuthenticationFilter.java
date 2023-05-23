package com.chanyongyang.hello.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
  @Autowired
  private TokenProvider tokenProvider;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    try {
      String token = parseBearerToken(request);

      if (token != null && !token.equalsIgnoreCase("null")) {
        String userId = tokenProvider.validateAndGetUserId(token);
        log.info("Authenticated userId : {}" + userId);

        // authentication 정보 생성 (설정정보 관여)
        // UsernamePasswordAuthenticationToken의 인자는 차례대로 id, pw, 권한 아래 코드는 id에 userId,
        // 비밀번호와 권한은 없다.
        // 없는 권한이라도 꼭 적어줘야 한다.
        AbstractAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userId, null,
            AuthorityUtils.NO_AUTHORITIES);
        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext(); // context 생성
        securityContext.setAuthentication(authenticationToken); // token 제작
        SecurityContextHolder.setContext(securityContext);
      }
    } catch (Exception e) {
      logger.error("security context에 인증된 사용자 정보 저장 불가", e);
    }
    filterChain.doFilter(request, response);
  }

  private String parseBearerToken(HttpServletRequest request) {
    String bearerToken = request.getHeader("Authorization");
    // Bearer: .... / 난수형태의 토큰값
    log.info("bearerToken : {}", bearerToken);

    if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
      // 토큰이 있으면 리턴
      String token = bearerToken.substring(7);
      log.info("read token {}", token);
      return token;
    }
    return null;
  }
}
