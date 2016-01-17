package org.hage.platform.util.fsm;


public interface CallableWithParameters<T> {

    void call(T parameter);
}
