package com.chanyongyang.hello.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chanyongyang.hello.model.TodoEntity;
import com.chanyongyang.hello.persistence.TodoRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class TodoService {

  @Autowired
  TodoRepository repository;

  public String testService() {
    TodoEntity entity = TodoEntity.builder().title("My first todo item").build();
    repository.save(entity); // insert
    TodoEntity savedEntity = repository.findById(entity.getId()).get(); // 단일조회

    return savedEntity.getTitle();
  }

  // final 키워드를 붙이는 이유 : 객체 불변 강제성을 위해
  public List<TodoEntity> create(final TodoEntity entity) {
    validate(entity);

    repository.save(entity);

    log.info("Entity ID : {} is saved", entity.getId());

    return repository.findByUserId(entity.getUserId());
  }

  public List<TodoEntity> retrieve(String userId) {
    return repository.findByUserId(userId);
  }

  public List<TodoEntity> update(final TodoEntity entity) {
    validate(entity);

    // TodoEntity origin = repository.findById(entity.getId()).get();
    // Optinal = null 체크 목적 아래 코드를 위와 같이 만들 수 있다. / orElse로 null일경우 대체값을 넣을 수 있다.
    final Optional<TodoEntity> origin = repository.findById(entity.getId());

    // ifPresent는 null이 아닐경우 처리해야되는 일을 정의한다.
    // 아래 코드는 title과 done 값만 변경하고 save한다.
    // id가 없으면 insert를, 있으면 update를 하게 만들었다.
    // 예시로 아래 코드에 setId를 null로 쓰게되면 insert 작업을 한다.
    // 함축된 코드를 잘 이해해보자
    origin.ifPresent(todo -> {
      todo.setTitle(entity.getTitle());
      todo.setDone(entity.isDone());
      repository.save(todo);
    });

    return retrieve(entity.getUserId());
  }

  public List<TodoEntity> delete(final TodoEntity entity) {
    validate(entity);
    try {
      repository.delete(entity);
    } catch (Exception e) {
      log.error("error deleting entity", entity.getId(), e);
      throw new RuntimeException("error deleting entity" + entity.getId());
    }

    // final Optional<TodoEntity> origin = repository.findById(entity.getId());

    // origin.ifPresent(todo -> {
    // todo.setTitle(entity.getTitle());
    // todo.setDone(entity.isDone());
    // repository.save(todo);
    // });

    return retrieve(entity.getUserId());
  }

  // final
  private void validate(final TodoEntity entity) {
    if (entity == null) {
      log.warn("Entity connot be null");
      throw new RuntimeException("Entity cannot be null");
    }

    if (entity.getUserId() == null) {
      log.warn("Unknown user.");
      throw new RuntimeException("Unknown user.");
    }
  }
}
