package com.infotecs.keyvaluestorage.service;

import com.infotecs.keyvaluestorage.model.StorageEntry;

import java.io.File;

public interface StorageService {

  StorageEntry findByKey(String key);

  StorageEntry save(StorageEntry storageEntry);

  StorageEntry delete(String key);

  File createDump(String path);

  void loadDump(String path);
}
