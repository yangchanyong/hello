package com.chanyongyang.hello.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.chanyongyang.hello.model.UserEntity;
import com.chanyongyang.hello.persistence.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class OauthUserServiceImpl extends DefaultOAuth2UserService {

  @Autowired
  private UserRepository userRepository;

  @Override
  public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
    OAuth2User oAuth2User = super.loadUser(userRequest);
    try {
      log.info("Oauth2 User Info {} ", new ObjectMapper().writeValueAsString(oAuth2User));
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }
    final String username = (String) oAuth2User.getAttribute("login");
    final String authProvider = userRequest.getClientRegistration().getClientName();

    // DB작업

    UserEntity userEntity = null;
    // db탐색 후 중복되는 id가 없을시 신규 id로 등록
    if (!userRepository.existsByUsername(username)) {
      userEntity = UserEntity.builder().username(username).authProvider(authProvider).build();
      userEntity = userRepository.save(userEntity);
    } else {
      userEntity = userRepository.findByUsername(username);
    }

    log.info("success pulled user info usernava {}, authProvider {}", username, authProvider);

    return new ApplicationOAuth2User(userEntity.getId(), oAuth2User.getAttributes());
  }
}
