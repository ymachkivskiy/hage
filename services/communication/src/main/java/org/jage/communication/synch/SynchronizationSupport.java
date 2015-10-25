package org.jage.communication.synch;


import com.google.common.util.concurrent.MoreExecutors;
import lombok.extern.slf4j.Slf4j;
import org.jage.communication.api.BaseRemoteChanel;
import org.jage.communication.message.ServiceMessage;
import org.jage.communication.message.consume.CompositeMessageConsumer;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


@Slf4j
public class SynchronizationSupport<ResultT, RemoteMessageT extends ServiceMessage> {

    private BaseRemoteChanel<RemoteMessageT> remotechanel;
    private BlockingQueue<RemoteMessageT> messageTs;

    private CompositeMessageConsumer<RemoteMessageT> messagesConsumers = new CompositeMessageConsumer<>();


    // synchronization message consumer aggregate (has all inner message consumers)

//    ExecutorService executorService = MoreExecutors.getExitingExecutorService(Executors.newFixedThreadPool(1));

    public SynchronizationSupport() {
//        Runtime.getRuntime().;
    }

    public ResultT sendToAll(RemoteMessageT remoteMessageT, MessagesAggregator<RemoteMessageT, ResultT> messagesAggregator) {
        return null;
    }

    public void registerToChanel(BaseRemoteChanel<RemoteMessageT> remoteChanel) {
        //TODO add our message consumer to channel
    }

    public void unregisterFromChanel(BaseRemoteChanel<RemoteMessageT> remoteChanel) {
        //TODO remove our message consumer from chanel
    }

    private class Response implements Callable<ResultT> {

        private final MessagesAggregator<RemoteMessageT, ResultT> messagesAggregator;

        private Response(MessagesAggregator<RemoteMessageT, ResultT> messagesAggregator) {
            this.messagesAggregator = messagesAggregator;
        }

        @Override
        public ResultT call() throws Exception {


            return null;
        }
    }

//    private class SynchShutdownHook extends
}
