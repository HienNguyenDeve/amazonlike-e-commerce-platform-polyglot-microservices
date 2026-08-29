package com.nguyenhien.user_service.assertions;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public final class JsonAssert {
  private static final ObjectMapper mapper = new ObjectMapper();

  private JsonAssert() {}

  public static void hasField(String json, String field) throws Exception {
    JsonNode node = mapper.readTree(json);
    assertThat(node.has(field)).isTrue();
  }

  public static void fieldEquals(String json, String field, String expected) throws Exception {
    JsonNode node = mapper.readTree(json);
    assertThat(node.get(field).asText()).isEqualTo(expected);
  }
}
