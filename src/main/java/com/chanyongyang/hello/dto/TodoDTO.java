package com.chanyongyang.hello.dto;

import com.chanyongyang.hello.model.TodoEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class TodoDTO {
  private String id;
  private String title;
  private boolean done;

  // entity > dto
  public TodoDTO(final TodoEntity entity) {
    this.id = entity.getId();
    this.title = entity.getTitle();
    this.done = entity.isDone();
  }

  // dto > entity
  public static TodoEntity todoEntity(final TodoDTO dto) {
    return TodoEntity.builder()
        .id(dto.getId())
        .title(dto.getTitle())
        .done(dto.isDone())
        .build();
  }
}
