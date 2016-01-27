package org.hage.platform.util.communication.synch;

import org.hage.platform.util.communication.message.service.ServiceMessage;

import java.util.List;
import java.util.function.Function;

public interface MessagesAggregator<RemoteMessageT extends ServiceMessage, ResultT> extends Function<List<RemoteMessageT>, ResultT> {
}
