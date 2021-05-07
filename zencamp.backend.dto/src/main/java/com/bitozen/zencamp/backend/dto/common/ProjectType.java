package com.bitozen.zencamp.backend.dto.common;

import java.util.Arrays;

/**
 *
 * @author amy
 */
public enum ProjectType {
    
    CQRS("CQRS"),
    CRUD("CRUD");

    private String name;

    private ProjectType(String name) {
        this.name = name;
    }

    public ProjectType findEnum(String name){
        return Arrays.stream(values())
                .filter(data -> data.name.equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }
}
