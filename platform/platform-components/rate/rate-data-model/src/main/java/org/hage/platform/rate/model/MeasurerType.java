package org.hage.platform.rate.model;

import java.io.Serializable;

public enum MeasurerType implements Serializable {

    CONCURRENCY_PERFORMANCE("concurrency"),
    MAX_RAM_MEMORY("rammemo"),
    ;

    private final String typeName;


    MeasurerType(String typeName) {
        this.typeName = typeName;
    }

    public String getTypeName() {
        return typeName;
    }
}
