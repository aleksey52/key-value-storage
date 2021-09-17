package com.infotecs.keyvaluestorage.repository.impl;

import com.infotecs.keyvaluestorage.model.StorageEntry;
import com.infotecs.keyvaluestorage.repository.StorageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class StorageRepositoryImpl implements StorageRepository {
    private final HashMap<String, StorageEntry> keyValueStorage = new HashMap<>();

    @Override
    public Optional<StorageEntry> findByKey(String key) {
        return Optional.ofNullable(keyValueStorage.get(key));
    }

    @Override
    public StorageEntry save(StorageEntry storageEntry) {
        log.warn("Save or Update entry with key: {}", storageEntry.getKey());
        return keyValueStorage.put(storageEntry.getKey(), storageEntry);
    }

    @Override
    public Boolean delete(String key) {
        return keyValueStorage.remove(key) != null;
    }

    @Override
    public HashMap<String, StorageEntry> findAll() {
        return keyValueStorage;
    }

    @Override
    public void saveAll(HashMap<String, StorageEntry> keyValueStorage) {
        this.keyValueStorage.clear();
        this.keyValueStorage.putAll(keyValueStorage);
    }
}