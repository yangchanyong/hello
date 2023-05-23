package com.chanyongyang.hello.service;

import org.apache.ibatis.ognl.security.UserMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.chanyongyang.hello.model.UserEntity;
import com.chanyongyang.hello.persistence.UserRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserService {
  @Autowired
  private UserRepository userRepository;

  public UserEntity create(final UserEntity userEntity) {
    if (userEntity == null || userEntity.getUsername() == null) {
      throw new RuntimeException("Invalid arguments");
    }
    final String username = userEntity.getUsername();
    if (userRepository.existsByUsername(username)) {
      String msg = "Username already exists";
      log.warn(msg + "{}", username);
      throw new RuntimeException(msg);
    }
    return userRepository.save(userEntity);
  }

  // 로그인
  public UserEntity getByCredentials(final String username, final String password, PasswordEncoder encoder) {

    // return userRepository.findByUsernameAndPassword(username, password); 기존코드
    UserEntity userEntity = userRepository.findByUsername(username);
    if (userEntity != null && encoder.matches(password, userEntity.getPassword())) {
      return userEntity;
    }
    return null;
  }

}
