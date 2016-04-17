package org.hage.util;

import java.util.*;

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

    public static <T> List<T> nullSafeCopy(List<T> original) {
        return ofNullable(original).map(ol -> (List<T>) new ArrayList<>(ol)).orElse(emptyList());
    }

    public static <T> Collection<T> nullSafeCopy(Collection<T> original) {
        return ofNullable(original).map(ol -> (List<T>) new ArrayList<>(ol)).orElse(emptyList());
    }

    public static <T> Set<T> nullSafeCopy(Set<T> original) {
        return ofNullable(original).map(os -> (Set<T>) new HashSet<>(os)).orElse(emptySet());
    }

    public static <K, V> Map<K, V> nullSafeCopy(Map<K, V> original) {
        return ofNullable(original).map(om -> (Map<K, V>) new HashMap<>(om)).orElse(emptyMap());
    }

}
