package com.chanyongyang.hello.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.chanyongyang.hello.dto.ResponseDTO;
import com.chanyongyang.hello.dto.TestRequestBodyDTO;

@RestController
@RequestMapping("test")
public class TestController {

  @GetMapping // 경로지정을 하지 않을경우 test를 따라감
  public String testController() {
    return "Hello World";
  }

  @GetMapping("/testGetMapping")
  public String testControllerWithPath() {
    return "hello world testGetMapping";
  }

  @GetMapping("{id}")
  public String testControllerWithPathVariables(@PathVariable String id) {
    return "Hello World id : " + id;
  }

  // 파라미터의 강제성여부를
  @GetMapping("requestParam")
  public String testControllerRequestParam(@RequestParam(required = false) int id) {
    return "Hello Woeld ID : " + id;
  }

  @GetMapping("requestBody")
  public String testControllerRequestBody(@RequestBody TestRequestBodyDTO testRequestBodyDTO) {
    return "Hello World id : " + testRequestBodyDTO.getId() + ", message : " + testRequestBodyDTO.getMessage();
  }

  @GetMapping("requestDTO")
  public String testControllerDTO(TestRequestBodyDTO testRequestBodyDTO) {
    return "Hello World id : " + testRequestBodyDTO.getId() + ", message : " + testRequestBodyDTO.getMessage();
  }

  // 반환 테스트
  @GetMapping("/testResponseBody")
  public ResponseDTO<String> testControllerResponseBody() {
    List<String> list = new ArrayList<>();
    list.add("hello world I'm responstDTO");
    ResponseDTO<String> responseDTO = ResponseDTO.<String>builder().data(list).build();
    return responseDTO;
  }

  @GetMapping("testResponseEntity")
  public ResponseEntity<?> testControllerResponseEntity() {
    List<String> list = new ArrayList<>();
    list.add("hello world I'm responstDTO");
    ResponseDTO<String> responseDTO = ResponseDTO.<String>builder().data(list).build();
    // return ResponseEntity.status(400).body(responseDTO);
    // return ResponseEntity.badRequest().body(responseDTO);
    return ResponseEntity.ok().body(responseDTO);

  }
}
