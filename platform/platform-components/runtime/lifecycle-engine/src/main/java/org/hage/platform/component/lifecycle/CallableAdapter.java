package org.hage.platform.component.lifecycle;


public class CallableAdapter<T> implements Runnable {

    private final CallableWithParameter<T> callable;

    private T parameter;

    public CallableAdapter(final CallableWithParameter<T> callable) {
        this.callable = callable;
    }

    public void setParameter(final T parameter) {
        this.parameter = parameter;
    }

    @Override
    public void run() {
        callable.call(parameter);
    }
}
