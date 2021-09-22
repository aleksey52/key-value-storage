package com.infotecs.keyvaluestorage;

import com.infotecs.keyvaluestorage.exception.DumpFileNotFoundException;
import com.infotecs.keyvaluestorage.exception.FailedOperationWithDumpFileException;
import com.infotecs.keyvaluestorage.exception.StorageEntryIllegalArgumentException;
import com.infotecs.keyvaluestorage.exception.StorageEntryNotFoundException;
import com.infotecs.keyvaluestorage.model.StorageEntry;
import com.infotecs.keyvaluestorage.repository.StorageRepository;
import com.infotecs.keyvaluestorage.service.StorageService;
import com.infotecs.keyvaluestorage.service.impl.StorageServiceImpl;
import com.infotecs.keyvaluestorage.service.scheduler.TaskService;
import org.json.simple.JSONObject;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.env.MockEnvironment;

import java.io.File;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@WebMvcTest(StorageService.class)
public class StorageServiceTest {

  @MockBean
  StorageRepository storageRepository;

  @MockBean
  TaskService taskService;

  JSONObject value_1 = new JSONObject(Map.of("value_part_1", 1, "value_part_2", "qwerty"));
  StorageEntry entry_1 = new StorageEntry("key1", value_1, null);

  JSONObject value_2 = new JSONObject(Map.of("value_part_1", "AAA", "value_part_2", "1337"));
  StorageEntry entry_2 = new StorageEntry("key2", value_2, 2);

  @Test
  public void getEntryByKey_success() {
    Mockito.when(storageRepository.findByKey(entry_1.getKey()))
        .thenReturn(java.util.Optional.of(entry_1));
    StorageService storageService = new StorageServiceImpl(storageRepository, taskService);
    StorageEntry resultingEntry = storageService.findByKey(entry_1.getKey());

    assertNotNull(resultingEntry);
    assertEquals(resultingEntry.getKey(), entry_1.getKey());
    assertEquals(resultingEntry.getValue(), entry_1.getValue());
    assertEquals(resultingEntry.getTtl(), entry_1.getTtl());
  }

  @Test
  public void getEntryByKey_notFound() {
    Mockito.when(storageRepository.findByKey(entry_1.getKey()))
        .thenReturn(java.util.Optional.empty());
    StorageService storageService = new StorageServiceImpl(storageRepository, taskService);

    try {
      storageService.findByKey(entry_1.getKey());
    } catch (Exception e) {
      assertTrue(e instanceof StorageEntryNotFoundException);
      assertEquals(e.getMessage(), "Storage entry with the specified key not found!");
    }
  }

  @Test
  public void setEntryByKey_success() {
    Mockito.when(storageRepository.save(entry_2)).thenReturn(null);
    Mockito.when(storageRepository.findByKey(entry_2.getKey()))
        .thenReturn(java.util.Optional.of(entry_2));
    StorageService storageService = new StorageServiceImpl(storageRepository, taskService);
    StorageEntry resultingEntry = storageService.save(entry_2);

    assertNotNull(resultingEntry);
    assertEquals(resultingEntry.getKey(), entry_2.getKey());
    assertEquals(resultingEntry.getValue(), entry_2.getValue());
    assertEquals(resultingEntry.getTtl(), entry_2.getTtl());
  }

  @Test
  public void setEntryByKey_nullKey() {
    StorageEntry entry = entry_2;
    entry.setKey(null);
    Mockito.when(storageRepository.save(entry)).thenReturn(null);
    Mockito.when(storageRepository.findByKey(entry.getKey()))
        .thenReturn(java.util.Optional.of(entry));
    StorageService storageService = new StorageServiceImpl(storageRepository, taskService);

    try {
      storageService.save(entry);
    } catch (Exception e) {
      assertTrue(e instanceof StorageEntryIllegalArgumentException);
      assertEquals(e.getMessage(), "Illegal arguments when creating a storage entry");
    }
  }

  @Test
  public void setEntryByKey_emptyKey() {
    StorageEntry entry = entry_2;
    entry.setKey("  ");
    Mockito.when(storageRepository.save(entry)).thenReturn(null);
    Mockito.when(storageRepository.findByKey(entry.getKey()))
        .thenReturn(java.util.Optional.of(entry));
    StorageService storageService = new StorageServiceImpl(storageRepository, taskService);

    try {
      storageService.save(entry);
    } catch (Exception e) {
      assertTrue(e instanceof StorageEntryIllegalArgumentException);
      assertEquals(e.getMessage(), "Illegal arguments when creating a storage entry");
    }
  }

  @Test
  public void removeEntryByKey_success() {
    Mockito.when(storageRepository.findByKey(entry_1.getKey()))
        .thenReturn(java.util.Optional.of(entry_1));
    Mockito.when(storageRepository.delete(entry_1.getKey())).thenReturn(entry_1);
    StorageService storageService = new StorageServiceImpl(storageRepository, taskService);
    StorageEntry removeEntry = storageService.delete(entry_1.getKey());

    assertNotNull(removeEntry);
    assertEquals(removeEntry.getKey(), entry_1.getKey());
    assertEquals(removeEntry.getValue(), entry_1.getValue());
    assertEquals(removeEntry.getTtl(), entry_1.getTtl());
  }

  @Test
  public void removeEntryByKey_notFound() {
    Mockito.when(storageRepository.findByKey(entry_1.getKey()))
        .thenReturn(java.util.Optional.empty());
    StorageService storageService = new StorageServiceImpl(storageRepository, taskService);

    try {
      storageService.delete(entry_1.getKey());
    } catch (Exception e) {
      assertTrue(e instanceof StorageEntryNotFoundException);
      assertEquals(e.getMessage(), "Storage entry with the specified key not found!");
    }
  }

  @Test
  public void createDump_success() {
    String filePath = "src\\main\\resources\\dump.txt";
    StorageService storageService = new StorageServiceImpl(storageRepository, taskService);
    File dump = storageService.createDump(filePath);

    assertNotNull(dump);
    assertEquals(dump.getPath(), filePath);
  }

  @Test
  public void createDump_emptyFilepath() {
    StorageService storageService = new StorageServiceImpl(storageRepository, taskService);

    try {
      storageService.createDump("");
    } catch (Exception e) {
      assertTrue(e instanceof FailedOperationWithDumpFileException);
      assertEquals(e.getMessage(), "Dump file creation error");
    }
  }

  @Test
  public void loadDump_success() {
    String dumpPath = "src\\main\\resources\\dump.txt";
    StorageService storageService = new StorageServiceImpl(storageRepository, taskService);
    storageService.loadDump(dumpPath);
  }

  @Test
  public void loadDump_fail() {
    String dumpPath = "src\\main\\resources\\unknown_file.txt";
    StorageService storageService = new StorageServiceImpl(storageRepository, taskService);

    try {
      storageService.loadDump(dumpPath);
    } catch (Exception e) {
      assertTrue(e instanceof DumpFileNotFoundException);
      assertEquals(e.getMessage(), "Dump file not found!");
    }
  }
}
