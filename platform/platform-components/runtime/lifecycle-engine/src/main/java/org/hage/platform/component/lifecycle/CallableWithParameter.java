package org.hage.platform.component.lifecycle;


public interface CallableWithParameter<T> {
    void call(T parameter);
}
