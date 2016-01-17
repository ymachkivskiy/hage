package org.hage.platform.util.fsm;


import javax.annotation.Nonnull;
import javax.annotation.Nullable;


public class CallableAdapter<T> implements Runnable {

    @Nonnull
    private final CallableWithParameters<T> callable;

    private T parameters;


    public CallableAdapter(final CallableWithParameters<T> callable) {
        super();
        this.callable = callable;
    }


    public void setParameters(@Nullable final T parameters) {
        this.parameters = parameters;
    }

    @Override
    public void run() {
        callable.call(parameters);
    }
}
