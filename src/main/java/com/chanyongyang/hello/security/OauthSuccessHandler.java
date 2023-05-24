package com.chanyongyang.hello.security;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class OauthSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
  private static final String LOCAL_REDIRECT_URL = "http://localhost:3000";
  // @Autowired
  // private TokenProvider tokenProvider;

  @Override
  public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
      Authentication authentication) throws IOException, ServletException {
    String token = new TokenProvider().create(authentication);
    log.info(token);
    // response.getWriter().write(token);
    // response.sendRedirect("socialLogin?token=" + token);

    Optional<Cookie> oCookie = Arrays.stream(request.getCookies())
        .filter(cookie -> cookie.getName().equals(RedirectUrlCookieFilter.REDIRECT_URL_PARAM)).findFirst();
    Optional<String> redirectUri = oCookie.map(Cookie::getValue);

    response.sendRedirect(redirectUri.orElseGet(() -> LOCAL_REDIRECT_URL) + "/socialLogin?token=" + token);
  }
}
