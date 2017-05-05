package org.hage.platform.util.connection.frame;

import lombok.Data;
import org.hage.platform.util.connection.frame.diagnostics.Diagnostics;

import java.io.Serializable;

@Data
public class Result {
    private final Serializable data;
    private final Diagnostics diagnostics;
}
