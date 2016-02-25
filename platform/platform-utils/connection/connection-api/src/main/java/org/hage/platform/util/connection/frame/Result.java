package org.hage.platform.util.connection.frame;

import lombok.Data;
import org.hage.platform.util.connection.frame.diagnostics.Diagnostics;

@Data
public class Result {
    private final Object data;
    private final Diagnostics diagnostics;
}
