package com.bitozen.zencamp.backend.dto.common;

import java.util.Arrays;

public enum DataStatus {

    ACTIVE("Active"),
    INACTIVE("Inactive");

    private String name;

    private DataStatus(String name) {
        this.name = name;
    }

    public DataStatus findEnum(String name){
        return Arrays.stream(values())
                .filter(data -> data.name.equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }
}
