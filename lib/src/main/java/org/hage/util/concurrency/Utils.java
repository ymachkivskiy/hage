package org.hage.util.concurrency;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@Slf4j
public class Utils {

    public static <T> T getWithBlocking(Future<T> future) {
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            log.error("Error during future get", e);
        }
        return null;
    }
}
