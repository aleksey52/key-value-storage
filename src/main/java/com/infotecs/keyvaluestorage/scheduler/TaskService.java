package com.infotecs.keyvaluestorage.scheduler;

import com.infotecs.keyvaluestorage.model.StorageEntry;
import com.infotecs.keyvaluestorage.repository.StorageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
@RequiredArgsConstructor
public class TaskService {
    private final StorageRepository storageRepository;
    private final ThreadPoolTaskScheduler threadPoolTaskScheduler;
    private final HashMap<String, ScheduledFuture<?>> scheduledFutureHashMap = new HashMap<>();

    public void addTask(StorageEntry storageEntry) {
        log.warn("start adding task");
        log.warn("pull size: " +
                String.valueOf(threadPoolTaskScheduler.getScheduledThreadPoolExecutor().getQueue().size()));
        ScheduledFuture<?> scheduledFuture = threadPoolTaskScheduler.schedule(
                new Task(storageRepository, storageEntry.getKey()),
                new Date(System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(storageEntry.getTtl())));
        scheduledFutureHashMap.put(storageEntry.getKey(), scheduledFuture);
        threadPoolTaskScheduler.schedule(() -> {
            log.error("START DELETING FROM HELPFUL HASHMAP");
            log.warn("WITH key: {}", storageEntry.getKey());
            log.warn("удаляемый элемент мапы: {}", scheduledFutureHashMap.get(storageEntry.getKey()));
            if (scheduledFutureHashMap.get(storageEntry.getKey()) !=
                    null && scheduledFutureHashMap.get(storageEntry.getKey()).isDone()) {
                scheduledFutureHashMap.remove(storageEntry.getKey());
            }
            log.warn("текущий размер вспомогательной мапы: {}", scheduledFutureHashMap.size());
            log.warn("текущее состояние вспомогательной мапы: {}", scheduledFutureHashMap);
            log.warn("текущий размер основной мапы: {}", storageRepository.findAll().size());
            log.warn("текущее состояние основной мапы: {}", storageRepository.findAll());
        }, new Date(System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(storageEntry.getTtl())));
        log.warn("ending");
        log.warn("pull size: " +
                String.valueOf(threadPoolTaskScheduler.getScheduledThreadPoolExecutor().getQueue().size()));
        log.warn("end add");
    }

    public void updateTask(StorageEntry storageEntry) {
        deleteTask(storageEntry.getKey());
        addTask(storageEntry);
    }

    public void deleteTask(String key) {
        log.warn("START CANCEL TASK");
        log.warn(String.valueOf(threadPoolTaskScheduler.getScheduledThreadPoolExecutor().getQueue().size()));
        if (scheduledFutureHashMap.get(key) != null && !scheduledFutureHashMap.get(key).isDone()) {
            log.warn("я попал в удаление");
            scheduledFutureHashMap.get(key).cancel(false);
        }
        log.warn("CANCELED");
        log.warn(String.valueOf(threadPoolTaskScheduler.getScheduledThreadPoolExecutor().getQueue().size()));
        log.warn("STOP CANCEL");
    }

    public void deleteAllTasks() {
        for (Map.Entry<String, StorageEntry> entry : storageRepository.findAll().entrySet()) {
            deleteTask(entry.getKey());
        }
    }
}
