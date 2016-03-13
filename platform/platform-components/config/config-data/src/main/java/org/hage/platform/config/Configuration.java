package org.hage.platform.config;

import lombok.Data;

import java.io.Serializable;

@Data
public class Configuration implements Serializable {
    private final Common common;
    private final Specific specific;
}
