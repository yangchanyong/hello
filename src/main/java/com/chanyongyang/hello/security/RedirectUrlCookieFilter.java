package com.chanyongyang.hello.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

@Service
public class RedirectUrlCookieFilter extends OncePerRequestFilter {
  public static final String REDIRECT_URL_PARAM = "redirect_url";
  private static final int MAX_AGE = 180;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    if (request.getRequestURI().startsWith("/oauth/auth")) {
      Cookie cookie = new Cookie(REDIRECT_URL_PARAM, request.getParameter(REDIRECT_URL_PARAM));
      cookie.setPath("/");
      cookie.setMaxAge(MAX_AGE);
      cookie.setHttpOnly(true);
      response.addCookie(cookie);
    }
    filterChain.doFilter(request, response);
  }
}
