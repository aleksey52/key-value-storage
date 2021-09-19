package com.infotecs.keyvaluestorage.model;

import lombok.*;
import org.json.simple.JSONObject;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class StorageEntry implements Serializable {
    private String key;
    private JSONObject value;
    private Integer ttl;
}
