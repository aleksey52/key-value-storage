package com.infotecs.keyvaluestorage.repository.impl;

import com.infotecs.keyvaluestorage.model.StorageEntry;
import com.infotecs.keyvaluestorage.repository.StorageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class StorageRepositoryImpl implements StorageRepository {
    private final HashMap<String, StorageEntry> keyValueStorage = new HashMap<>();

    @Override
    public Optional<StorageEntry> findByKey(String key) {
        return Optional.ofNullable(keyValueStorage.get(key));
    }

    @Override
    public StorageEntry save(StorageEntry storageEntry) {
        return keyValueStorage.put(storageEntry.getKey(), storageEntry);
    }

    @Override
    public StorageEntry delete(String key) {
        return keyValueStorage.remove(key);
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
