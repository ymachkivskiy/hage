package org.hage.util;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static java.util.Collections.*;
import static java.util.Optional.ofNullable;

public class CollectionUtils {

    public static <T> Collection<T> nullSafe(Collection<T> original) {
        return ofNullable(original).orElse(emptyList());
    }

    public static <T> List<T> nullSafe(List<T> original) {
        return ofNullable(original).orElse(emptyList());
    }

    public static <T> Set<T> nullSafe(Set<T> original) {
        return ofNullable(original).orElse(emptySet());
    }

    public static <K, V> Map<K, V> nullSafe(Map<K, V> original) {
        return ofNullable(original).orElse(emptyMap());
    }

}
