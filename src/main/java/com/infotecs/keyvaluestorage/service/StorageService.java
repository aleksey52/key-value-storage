package com.infotecs.keyvaluestorage.service;

import com.infotecs.keyvaluestorage.model.StorageEntry;

public interface StorageService {
    StorageEntry findByKey(String key);

    StorageEntry save(StorageEntry storageEntry);

    Boolean delete(String key);

    void createDump();

    void loadDump();
}
