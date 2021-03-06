package com.infotecs.keyvaluestorage.controller;

import com.infotecs.keyvaluestorage.model.DumpFile;
import com.infotecs.keyvaluestorage.model.StorageEntry;
import com.infotecs.keyvaluestorage.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;

@RestController
@RequestMapping("/storage/api")
public class StorageController {

  private final StorageService storageService;

  @Autowired
  public StorageController(StorageService storageService) {
    this.storageService = storageService;
  }

  @GetMapping("/{key}")
  public ResponseEntity<StorageEntry> getByKey(@PathVariable("key") String key) {
    return new ResponseEntity<>(storageService.findByKey(key), HttpStatus.OK);
  }

  @PostMapping(consumes = "application/json", produces = "application/json")
  public ResponseEntity<StorageEntry> setByKey(
      @RequestBody(required = false) StorageEntry storageEntry) {
    return new ResponseEntity<>(storageService.save(storageEntry), HttpStatus.OK);
  }

  @DeleteMapping("/{key}")
  public ResponseEntity<StorageEntry> removeByKey(@PathVariable("key") String key) {
    return new ResponseEntity<>(storageService.delete(key), HttpStatus.OK);
  }

  @PostMapping("/dump")
  public ResponseEntity<DumpFile> createDump(@RequestBody DumpFile dumpFile) {
    dumpFile.setPath(storageService.createDump(dumpFile.getPath()).getPath());
    return new ResponseEntity<>(dumpFile, HttpStatus.OK);
  }

  @PostMapping("/load")
  public ResponseEntity<?> loadDump(@RequestBody DumpFile dumpFile) {
    storageService.loadDump(dumpFile.getPath());
    return new ResponseEntity<>(HttpStatus.OK);
  }
}
