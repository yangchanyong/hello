package com.chanyongyang.hello.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.chanyongyang.hello.dto.ResponseDTO;
import com.chanyongyang.hello.dto.TodoDTO;
import com.chanyongyang.hello.model.TodoEntity;
import com.chanyongyang.hello.service.TodoService;

@RestController
@RequestMapping("todo")
public class TodoController {

  @Autowired
  private TodoService service;

  @GetMapping("todoEntity")
  public ResponseEntity<?> testTodo() {
    List<String> list = new ArrayList<>();
    list.add(service.testService());
    list.add(service.testService());
    list.add(service.testService());
    list.add(service.testService());
    return ResponseEntity.ok().body(ResponseDTO.<String>builder().data(list).build());
  }

  @PostMapping
  public ResponseEntity<?> createTodo(@RequestBody TodoDTO dto) {
    try {
      String temporaryUserId = "temporary-user";

      TodoEntity entity = TodoDTO.todoEntity(dto);
      entity.setId(null);
      entity.setUserId(temporaryUserId);

      List<TodoEntity> entities = service.create(entity);

      List<TodoDTO> dtos = entities.stream().map(e -> new TodoDTO(e)).collect(Collectors.toList());
      return ResponseEntity.ok().body(ResponseDTO.<TodoDTO>builder().data(dtos).build());

    } catch (Exception e) {
      return ResponseEntity.badRequest().body(ResponseDTO.<TodoDTO>builder().error(e.getMessage()).build());
    }
  }

  @PutMapping
  public ResponseEntity<?> updateTodo(@RequestBody TodoDTO dto) {
    try {
      // 조회의 목적으로 userId를 사용
      String temporaryUserId = "temporary-user";
      TodoEntity entity = TodoDTO.todoEntity(dto);
      entity.setUserId(temporaryUserId);

      List<TodoEntity> entities = service.update(entity);

      List<TodoDTO> dtos = entities.stream().map(e -> new TodoDTO(e)).collect(Collectors.toList());
      return ResponseEntity.ok().body(ResponseDTO.<TodoDTO>builder().data(dtos).build());

    } catch (Exception e) {
      return ResponseEntity.badRequest().body(ResponseDTO.<TodoDTO>builder().error(e.getMessage()).build());
    }
  }

  @DeleteMapping
  public ResponseEntity<?> deleteTodo(@RequestBody TodoDTO dto) {
    String str = "1234";
    try {
      String temporaryUserId = "temporary-user";
      TodoEntity entity = TodoDTO.todoEntity(dto);
      entity.setUserId(temporaryUserId);

      List<TodoEntity> entities = service.delete(entity);

      List<TodoDTO> dtos = entities.stream().map(e -> new TodoDTO(e)).collect(Collectors.toList());
      return ResponseEntity.ok().body(ResponseDTO.<TodoDTO>builder().data(dtos).build());

    } catch (Exception e) {
      return ResponseEntity.badRequest().body(ResponseDTO.<TodoDTO>builder().error(e.getMessage()).build());
    }
  }

  @GetMapping
  public ResponseEntity<?> retrieveTodoList() {
    try {
      String temporaryUserId = "temporary-user";

      List<TodoEntity> entities = service.retrieve(temporaryUserId);

      List<TodoDTO> dtos = entities.stream().map(e -> new TodoDTO(e)).collect(Collectors.toList());
      return ResponseEntity.ok().body(ResponseDTO.<TodoDTO>builder().data(dtos).build());

    } catch (Exception e) {
      e.getMessage();
      return ResponseEntity.badRequest().body(ResponseDTO.<TodoDTO>builder().error(e.getMessage()).build());
    }
  }
}
