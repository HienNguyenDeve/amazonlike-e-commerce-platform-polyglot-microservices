package com.nguyenhien.user_service.assertions;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.springframework.test.web.servlet.ResultActions;

public final class ApiAssert {
  private ApiAssert() {}

  public static void ok(ResultActions result) throws Exception {
    result.andExpect(status().isOk());
  }

  public static void created(ResultActions result) throws Exception {
    result.andExpect(status().isCreated());
  }

  public static void badRequest(ResultActions result) throws Exception {
    result.andExpect(status().isBadRequest());
  }

  public static void notFound(ResultActions result) throws Exception {
    result.andExpect(status().isNotFound());
  }
}
