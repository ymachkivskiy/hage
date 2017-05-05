package org.hage.platform.cluster.connection.frame.util;


import org.hage.platform.cluster.connection.frame.Result;

import static org.hage.platform.cluster.connection.frame.diagnostics.ResultType.SUCCESS;

public class ResultUtil {

    public static boolean isSuccessful(Result result) {
        return result.getDiagnostics().getResultType() == SUCCESS;
    }

}
