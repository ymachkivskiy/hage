package org.hage.platform.util.connection.frame.diagnostics;

import lombok.Data;

import java.io.Serializable;

@Data
public class Diagnostics implements Serializable {
    public static transient final Diagnostics SUCCESS_DIAGNOSTICS = new Diagnostics(ResultType.SUCCESS, null);

    private final ResultType resultType;
    private final String errorMessage;


}
