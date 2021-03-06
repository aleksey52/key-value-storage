package com.infotecs.keyvaluestorage.service.impl;

import com.infotecs.keyvaluestorage.exception.DumpFileNotFoundException;
import com.infotecs.keyvaluestorage.exception.FailedOperationWithDumpFileException;
import com.infotecs.keyvaluestorage.exception.StorageEntryIllegalArgumentException;
import com.infotecs.keyvaluestorage.exception.StorageEntryNotFoundException;
import com.infotecs.keyvaluestorage.model.StorageEntry;
import com.infotecs.keyvaluestorage.repository.StorageRepository;
import com.infotecs.keyvaluestorage.service.StorageService;
import com.infotecs.keyvaluestorage.service.scheduler.TaskService;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StorageServiceImpl implements StorageService {

  private final StorageRepository storageRepository;
  private final TaskService taskService;
  private static final Integer DEFAULT_TTL = 60;

  @Override
  public StorageEntry findByKey(String key) {
    return storageRepository.findByKey(key).orElseThrow(() ->
        new StorageEntryNotFoundException("Storage entry with the specified key not found!"));
  }

  @Override
  public StorageEntry save(StorageEntry storageEntry) {
    if (storageEntry == null || storageEntry.getKey() == null) {
      throw new StorageEntryIllegalArgumentException(
          "Illegal arguments when creating a storage entry");
    } else {
      storageEntry.setKey(storageEntry.getKey().trim());
      if (storageEntry.getValue() == null
          || storageEntry.getKey().isEmpty()
          || storageEntry.getValue().isEmpty()) {
        throw new StorageEntryIllegalArgumentException(
            "Illegal arguments when creating a storage entry");
      }
    }

    if (storageEntry.getTtl() == null) {
      storageEntry.setTtl(DEFAULT_TTL);
    }
    StorageEntry pastEntry = storageRepository.save(storageEntry);
    if (pastEntry == null) {
      taskService.addTask(storageEntry);
    } else {
      taskService.updateTask(storageEntry);
    }

    return findByKey(storageEntry.getKey());
  }

  @Override
  public StorageEntry delete(String key) {
    findByKey(key);
    taskService.deleteTask(key);

    return storageRepository.delete(key);
  }

  @Override
  public File createDump(String path) {
    File dumpFile;
    try {
      dumpFile = new File(path);
      if (!dumpFile.exists()) {
        dumpFile.createNewFile();
      }
      ObjectOutputStream os = new ObjectOutputStream(
          new FileOutputStream(path));
      os.writeObject(storageRepository.findAll());
    } catch (IOException | NullPointerException e) {
      throw new FailedOperationWithDumpFileException("Dump file creation error");
    }

    return dumpFile;
  }

  @Override
  @SuppressWarnings("unchecked")
  public void loadDump(String path) {
    taskService.deleteAllTasks();
    try (ObjectInputStream is = new ObjectInputStream(
        new FileInputStream(path))) {
      storageRepository.saveAll((HashMap<String, StorageEntry>) is.readObject());
    } catch (FileNotFoundException e) {
      throw new DumpFileNotFoundException("Dump file not found!");
    } catch (IOException | ClassNotFoundException e) {
      throw new FailedOperationWithDumpFileException("Dump file loading error");
    }

    for (Map.Entry<String, StorageEntry> entry : storageRepository.findAll().entrySet()) {
      taskService.addTask(entry.getValue());
    }
  }
}
