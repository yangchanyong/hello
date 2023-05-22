package com.chanyongyang.hello.config;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

@SuppressWarnings("deprecation")
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http
        .cors()
        .and().csrf().disable().httpBasic().disable().sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // session 안씀
        .and().authorizeRequests()
        .antMatchers("/", "/auth/**").permitAll()
        .anyRequest().authenticated()
    // .and().addFilterAfter()
    ;
  }
}
