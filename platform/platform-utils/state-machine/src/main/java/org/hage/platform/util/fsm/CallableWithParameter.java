package org.hage.platform.util.fsm;


public interface CallableWithParameter<T> {
    void call(T parameter);
}
