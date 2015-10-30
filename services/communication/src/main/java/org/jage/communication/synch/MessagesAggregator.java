package org.jage.communication.synch;

import org.jage.communication.message.service.ServiceMessage;

import java.util.List;
import java.util.function.Function;

public interface MessagesAggregator<RemoteMessageT extends ServiceMessage, ResultT> extends Function<List<RemoteMessageT>, ResultT> {
}
