package org.hage.platform.communication.synch;

import org.hage.platform.communication.message.service.ServiceMessage;

import java.util.List;
import java.util.function.Function;

public interface MessagesAggregator<RemoteMessageT extends ServiceMessage, ResultT> extends Function<List<RemoteMessageT>, ResultT> {
}
