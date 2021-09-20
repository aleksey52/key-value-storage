package com.infotecs.keyvaluestorage.repository;

import com.infotecs.keyvaluestorage.model.StorageEntry;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Optional;

@Repository
public interface StorageRepository {
    Optional<StorageEntry> findByKey(String key);

    StorageEntry save(StorageEntry storageEntry);

    StorageEntry delete(String key);

    HashMap<String, StorageEntry> findAll();

    void saveAll(HashMap<String, StorageEntry> keyValueStorage);
}
