package com.infotecs.keyvaluestorage.service.scheduler;

import com.infotecs.keyvaluestorage.model.StorageEntry;
import com.infotecs.keyvaluestorage.repository.StorageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final StorageRepository storageRepository;
    private final ThreadPoolTaskScheduler threadPoolTaskScheduler;
    private final HashMap<String, ScheduledFuture<?>> scheduledFutureHashMap = new HashMap<>();

    public void addTask(StorageEntry storageEntry) {
        ScheduledFuture<?> scheduledFuture = threadPoolTaskScheduler.schedule(
                new Task(storageRepository, storageEntry.getKey()),
                new Date(System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(storageEntry.getTtl())));
        scheduledFutureHashMap.put(storageEntry.getKey(), scheduledFuture);
        threadPoolTaskScheduler.schedule(() -> {
            if (scheduledFutureHashMap.get(storageEntry.getKey()) != null
                    && scheduledFutureHashMap.get(storageEntry.getKey()).isDone()) {
                scheduledFutureHashMap.remove(storageEntry.getKey());
            }
        }, new Date(System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(storageEntry.getTtl())));
    }

    public void updateTask(StorageEntry storageEntry) {
        deleteTask(storageEntry.getKey());
        addTask(storageEntry);
    }

    public void deleteTask(String key) {
        if (scheduledFutureHashMap.get(key) != null && !scheduledFutureHashMap.get(key).isDone()) {
            scheduledFutureHashMap.get(key).cancel(false);
        }
    }

    public void deleteAllTasks() {
        for (Map.Entry<String, StorageEntry> entry : storageRepository.findAll().entrySet()) {
            deleteTask(entry.getKey());
        }
    }
}
