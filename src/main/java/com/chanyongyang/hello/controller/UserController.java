package com.chanyongyang.hello.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.chanyongyang.hello.dto.ResponseDTO;
import com.chanyongyang.hello.dto.UserDTO;
import com.chanyongyang.hello.model.UserEntity;
import com.chanyongyang.hello.security.TokenProvider;
import com.chanyongyang.hello.service.UserService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("auth")
public class UserController {
  @Autowired
  private UserService userService;

  @Autowired
  private TokenProvider tokenProvider;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @PostMapping("signup")
  public ResponseEntity<?> registerUser(@RequestBody UserDTO userDTO) {
    try {
      if (userDTO == null || userDTO.getPassword() == null) {
        throw new RuntimeException("Invalid password value");
      }
      UserEntity userEntity = UserEntity.builder().username(userDTO.getUsername())
          .password(passwordEncoder.encode(userDTO.getPassword()))
          .build();
      UserEntity registerUser = userService.create(userEntity);
      UserDTO responseUserDTO = UserDTO.builder().id(registerUser.getId()).username(registerUser.getUsername()).build();
      return ResponseEntity.ok().body(responseUserDTO);
    } catch (Exception e) {
      ResponseDTO<?> responseDTO = ResponseDTO.builder().error(e.getMessage()).build();
      return ResponseEntity.badRequest().body(responseDTO);
    }
  }

  @PostMapping("signin")
  public ResponseEntity<?> authenticate(@RequestBody UserDTO userDTO) {
    log.info("{}", userDTO);
    UserEntity user = userService.getByCredentials(userDTO.getUsername(), userDTO.getPassword(), passwordEncoder);
    if (user != null) {
      String token = tokenProvider.create(user);
      final UserDTO responseUserDTO = UserDTO.builder()
          .username(user.getUsername())
          .id(user.getId())
          .token(token)
          .build();
      return ResponseEntity.ok().body(responseUserDTO);
    } else {
      ResponseDTO<?> responseDTO = ResponseDTO.builder().error("Login failed").build();
      return ResponseEntity.badRequest().body(responseDTO);
    }
  }
}
