package org.hage.communication.synch;

import org.hage.communication.message.service.ServiceMessage;

import java.util.List;
import java.util.function.Function;

public interface MessagesAggregator<RemoteMessageT extends ServiceMessage, ResultT> extends Function<List<RemoteMessageT>, ResultT> {
}
