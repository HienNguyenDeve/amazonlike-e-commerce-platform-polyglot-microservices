package com.nguyenhien.user_service.integration;

import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@AutoConfigureMockMvc
public abstract class BaseControllerIT extends BaseIntegrationTest {
  @Autowired protected MockMvc mockMvc;

  /*
   * =========================
   * GET
   * =========================
   */

  protected ResultActions get(String url) throws Exception {

    return mockMvc.perform(MockMvcRequestBuilders.get(url));
  }

  protected ResultActions get(String url, Map<String, String> headers) throws Exception {

    MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get(url);

    headers.forEach(request::header);

    return mockMvc.perform(request);
  }

  /*
   * =========================
   * POST
   * =========================
   */

  protected ResultActions post(String url, Object body) throws Exception {

    return mockMvc.perform(
        MockMvcRequestBuilders.post(url)
            .contentType(MediaType.APPLICATION_JSON)
            .content(toJson(body)));
  }

  protected ResultActions post(String url, Object body, Map<String, String> headers)
      throws Exception {

    MockHttpServletRequestBuilder request =
        MockMvcRequestBuilders.post(url)
            .contentType(MediaType.APPLICATION_JSON)
            .content(toJson(body));

    headers.forEach(request::header);

    return mockMvc.perform(request);
  }

  /*
   * =========================
   * PUT
   * =========================
   */

  protected ResultActions put(String url, Object body) throws Exception {

    return mockMvc.perform(
        MockMvcRequestBuilders.put(url)
            .contentType(MediaType.APPLICATION_JSON)
            .content(toJson(body)));
  }

  protected ResultActions put(String url, Object body, Map<String, String> headers)
      throws Exception {

    MockHttpServletRequestBuilder request =
        MockMvcRequestBuilders.put(url)
            .contentType(MediaType.APPLICATION_JSON)
            .content(toJson(body));

    headers.forEach(request::header);

    return mockMvc.perform(request);
  }

  /*
   * =========================
   * PATCH
   * =========================
   */

  protected ResultActions patch(String url, Object body) throws Exception {

    return mockMvc.perform(
        MockMvcRequestBuilders.patch(url)
            .contentType(MediaType.APPLICATION_JSON)
            .content(toJson(body)));
  }

  /*
   * =========================
   * DELETE
   * =========================
   */

  protected ResultActions delete(String url) throws Exception {

    return mockMvc.perform(MockMvcRequestBuilders.delete(url));
  }

  protected ResultActions delete(String url, Map<String, String> headers) throws Exception {

    MockHttpServletRequestBuilder request = MockMvcRequestBuilders.delete(url);

    headers.forEach(request::header);

    return mockMvc.perform(request);
  }
}
