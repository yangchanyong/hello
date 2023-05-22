package com.chanyongyang.hello.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import com.chanyongyang.hello.model.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, String> {

  UserEntity findByUsername(String username);

  Boolean existsByUsername(String username);

  UserEntity findByUsernameAndPassword(String username, String password);
}
