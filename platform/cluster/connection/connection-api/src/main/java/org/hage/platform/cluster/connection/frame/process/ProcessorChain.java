package org.hage.platform.cluster.connection.frame.process;

import org.hage.platform.cluster.connection.frame.Frame;

import java.util.ArrayList;
import java.util.List;

public class ProcessorChain implements FrameProcessor {

    private final List<FrameProcessor> processors;

    public ProcessorChain(List<FrameProcessor> processors) {
        this.processors = new ArrayList<>(processors);
    }

    @Override
    public Frame process(Frame input) {

        Frame current = input;

        for (FrameProcessor processor : processors) {
            current = processor.process(current);
        }

        return current;
    }

    public void addProcessor(FrameProcessor processor) {
        processors.add(processor);
    }

    public void addProcessors(List<FrameProcessor> processors) {
        this.processors.addAll(processors);
    }

}
