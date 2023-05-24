package com.chanyongyang.hello.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestRedirectFilter;
import org.springframework.security.web.authentication.Http403ForbiddenEntryPoint;
import org.springframework.web.filter.CorsFilter;

import com.chanyongyang.hello.security.JwtAuthenticationFilter;
import com.chanyongyang.hello.security.OauthSuccessHandler;
import com.chanyongyang.hello.security.OauthUserServiceImpl;
import com.chanyongyang.hello.security.RedirectUrlCookieFilter;

@SuppressWarnings("deprecation")
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
  @Autowired
  private JwtAuthenticationFilter jwtAuthenticationFilter;
  @Autowired
  private OauthUserServiceImpl oauthUserServiceImpl;
  @Autowired
  private OauthSuccessHandler oAuthSuccessHandler;
  @Autowired
  private RedirectUrlCookieFilter redirectUrlCookieFilter;

  @Override
  protected void configure(HttpSecurity http) throws Exception {

    http
        .cors()
        .and().csrf().disable().httpBasic().disable().sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // session 안씀
        .and().authorizeRequests()
        .antMatchers("/", "/auth/**", "/oauth2/**").permitAll()
        .anyRequest().authenticated()
        .and()
        .oauth2Login()
        .authorizationEndpoint().baseUri("/oauth2/auth")
        .and()
        .redirectionEndpoint().baseUri("/oauth2/callback/*")
        .and()
        .userInfoEndpoint()
        .userService(oauthUserServiceImpl)
        .and()
        .successHandler(oAuthSuccessHandler)
        .and()
        .exceptionHandling()
        .authenticationEntryPoint(new Http403ForbiddenEntryPoint());

    // http.oauth2Login();
    http.addFilterAfter(jwtAuthenticationFilter, CorsFilter.class);
    // http.addFilterBefore(cookieFilter,
    // OAuth2AuthorizationRequestRedirectFilter.class);
  }

  @Bean
  public PasswordEncoder getPasswordEncoder() {
    return new BCryptPasswordEncoder();
  }
}
