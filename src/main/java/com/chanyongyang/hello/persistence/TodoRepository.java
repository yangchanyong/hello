package com.chanyongyang.hello.persistence;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.chanyongyang.hello.model.TodoEntity;

@Repository
public interface TodoRepository extends JpaRepository<TodoEntity, String> {
  // @Query // 커스텀쿼리
  List<TodoEntity> findByUserId(String userId);
}
