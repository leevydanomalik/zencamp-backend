package com.bitozen.zencamp.backend.dto.common;

import java.util.Arrays;

/**
 *
 * @author amy
 */
public enum ResponseStatus {
    S("Success"),
    F("Failed");

    private String name;

    private ResponseStatus(String name) {
        this.name = name;
    }

    public ResponseStatus findEnum(String name){
        return Arrays.stream(values())
                .filter(data -> data.name.equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }
}
