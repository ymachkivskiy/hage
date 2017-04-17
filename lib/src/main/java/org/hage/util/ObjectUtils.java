package org.hage.util;

import java.util.Optional;

import static java.util.Arrays.asList;

public class ObjectUtils {

    public static boolean allNotNull(Object... objects) {
        return !asList(objects)
            .stream()
            .map(Optional::ofNullable)
            .filter(o -> !o.isPresent())
            .findFirst()
            .isPresent();
    }

//    public static boolean hasAtLeastOneNull(Object... objects) {
//
//    }
}
