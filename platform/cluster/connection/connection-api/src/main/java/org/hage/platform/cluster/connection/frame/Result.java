package org.hage.platform.cluster.connection.frame;

import lombok.Data;
import org.hage.platform.cluster.connection.frame.diagnostics.Diagnostics;

import java.io.Serializable;

@Data
public class Result {
    private final Serializable data;
    private final Diagnostics diagnostics;
}
