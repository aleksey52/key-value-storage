package com.infotecs.keyvaluestorage;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.infotecs.keyvaluestorage.controller.StorageController;
import com.infotecs.keyvaluestorage.model.StorageEntry;
import com.infotecs.keyvaluestorage.service.StorageService;
import org.json.simple.JSONObject;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.io.File;
import java.util.Map;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(StorageController.class)
public class StorageControllerTest {

  @Autowired
  MockMvc mockMvc;
  @Autowired
  ObjectMapper objectMapper;

  @MockBean
  StorageService storageService;

  JSONObject value_1 = new JSONObject(Map.of("value_part_1", 1, "value_part_2", "qwerty"));
  StorageEntry entry_1 = new StorageEntry("key1", value_1, null);

  JSONObject value_2 = new JSONObject(Map.of("value_part_1", "AAA", "value_part_2", "1337"));
  StorageEntry entry_2 = new StorageEntry("key2", value_2, 2);

  @Test
  public void getEntryByKey_success() throws Exception {
    Mockito.when(storageService.findByKey(entry_1.getKey())).thenReturn(entry_1);
    mockMvc.perform(MockMvcRequestBuilders
        .get("/storage/api/" + entry_1.getKey())
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", notNullValue()))
        .andExpect(jsonPath("$.key", is(entry_1.getKey())))
        .andExpect(jsonPath("$.value", is(value_1)))
        .andExpect(jsonPath("$.ttl", is(entry_1.getTtl())));
  }

  @Test
  public void setEntryByKey_success() throws Exception {
    Mockito.when(storageService.save(entry_1)).thenReturn(entry_1);

    MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/storage/api")
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .content(this.objectMapper.writeValueAsString(entry_1));

    mockMvc.perform(mockRequest)
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", notNullValue()))
        .andExpect(jsonPath("$.key", is(entry_1.getKey())))
        .andExpect(jsonPath("$.value", is(value_1)))
        .andExpect(jsonPath("$.ttl", is(entry_1.getTtl())));
  }

  @Test
  public void removeEntryByKey_success() throws Exception {
    Mockito.when(storageService.delete(entry_2.getKey())).thenReturn(entry_2);

    mockMvc.perform(MockMvcRequestBuilders
        .delete("/storage/api/" + entry_2.getKey())
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", notNullValue()))
        .andExpect(jsonPath("$.key", is(entry_2.getKey())))
        .andExpect(jsonPath("$.value", is(value_2)))
        .andExpect(jsonPath("$.ttl", is(entry_2.getTtl())));
  }

  @Test
  public void createDump_success() throws Exception {
    String path = "src\\main\\resources\\";
    String filePath = "src\\main\\resources\\dump.txt";
    File dumpFile = new File(filePath);
    Mockito.when(storageService.createDump(path)).thenReturn(dumpFile);

    MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders
        .post("/storage/api/dump")
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .content(path);

    mockMvc.perform(mockRequest)
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", notNullValue()))
        .andExpect(jsonPath("$", is(dumpFile.getAbsolutePath())));
  }

  @Test
  public void loadDump_success() throws Exception {
    String dumpPath = "src\\main\\resources\\dump.txt";
    File dumpFile = new File(dumpPath);

    MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/storage/api/load")
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .content(this.objectMapper.writeValueAsString(dumpFile));

    mockMvc.perform(mockRequest)
        .andExpect(status().isOk());
  }
}
