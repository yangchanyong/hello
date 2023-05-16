package com.chanyongyang.hello;

import lombok.Builder;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Builder
@RequiredArgsConstructor // nonnull인 필드를 가진 애들을 대상으로 생성자를 생성만듬
@ToString
public class DemoModel {
  @NonNull
  private String id;

  public static void main(String[] args) {
    DemoModel demoModel = DemoModel.builder().id("abcd").build();
    System.out.println(demoModel);
  }

}
