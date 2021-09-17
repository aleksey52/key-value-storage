package com.infotecs.keyvaluestorage.scheduler;

import com.infotecs.keyvaluestorage.repository.StorageRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@AllArgsConstructor
@Getter
@Setter
public class Task implements Runnable {
    private StorageRepository storageRepository;
    private String key;

    @Override
    public void run() {
        storageRepository.delete(key);
    }
}
