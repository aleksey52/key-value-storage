package com.infotecs.keyvaluestorage.scheduler;

import com.infotecs.keyvaluestorage.repository.StorageRepository;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
@Getter
@Setter
public class Task implements Runnable {
    private StorageRepository storageRepository;
    private String key;

    public Task(StorageRepository storageRepository, String key) {
        this.storageRepository = storageRepository;
        this.key = key;
    }

//    @Autowired
//    public void setStorageRepository(StorageRepository storageRepository) {
//        this.storageRepository = storageRepository;
//    }

    @Override
    public void run() {
        log.error("Start task with key: {}", key);
        storageRepository.delete(key);
        log.warn("текущее состояние основной мапы: {}", storageRepository.findAll());
        log.error("End task with key: {}", key);
    }
}
