package org.hage.platform.util.connection.data;

import lombok.RequiredArgsConstructor;

import java.io.Serializable;

@RequiredArgsConstructor
public class Payload implements Serializable {

    private final Object data;

    public <T> T getData(Class<T> clazz) {
        if (data.getClass() != clazz) {
            return null;
        }
        return clazz.cast(data);
    }
}
