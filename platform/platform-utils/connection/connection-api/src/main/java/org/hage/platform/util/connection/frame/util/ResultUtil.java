package org.hage.platform.util.connection.frame.util;


import org.hage.platform.util.connection.frame.Result;

import static org.hage.platform.util.connection.frame.diagnostics.ResultType.SUCCESS;

public class ResultUtil {

    public static boolean isSuccessful(Result result) {
        return result.getDiagnostics().getResultType() == SUCCESS;
    }

}
