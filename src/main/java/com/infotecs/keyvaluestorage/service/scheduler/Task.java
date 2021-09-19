package com.infotecs.keyvaluestorage.service.scheduler;

import com.infotecs.keyvaluestorage.repository.StorageRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

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
