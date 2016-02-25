package org.hage.platform.util.connection.adapter.aggregate;

import org.hage.platform.util.connection.frame.Frame;

import java.util.Collection;

public interface FrameAggregator<ResultT> {

    ResultT aggregate(Collection<Frame> frames);

}
