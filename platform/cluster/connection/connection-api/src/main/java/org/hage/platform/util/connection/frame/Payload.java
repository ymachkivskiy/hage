package org.hage.platform.util.connection.frame;

import lombok.RequiredArgsConstructor;

import java.io.Serializable;

import static com.google.common.base.Preconditions.checkArgument;

@RequiredArgsConstructor
public class Payload implements Serializable {

    private final Object data;

    public <T> T getData(Class<T> requestedClazz) {
        checkArgument(checkDataType(requestedClazz), "Payload data has type %s but %s acquired", data.getClass(), requestedClazz);
        return requestedClazz.cast(data);
    }

    public <T> boolean checkDataType(Class<T> checkingType) {
        return data != null && data.getClass() == checkingType;
    }

    @Override
    public String toString() {
        return "Payload with [" + data.getClass().getSimpleName() + "]";
    }
}
