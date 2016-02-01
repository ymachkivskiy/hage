package org.hage.examples.lifecycle;


import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import org.hage.platform.annotation.FieldsAreNonnullByDefault;
import org.hage.platform.component.IStatefulComponent;
import org.hage.platform.component.lifecycle.event.LifecycleStateChangedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static java.lang.String.format;

@FieldsAreNonnullByDefault
public class LifecycleTransitionLogger implements IStatefulComponent {

    private static final Logger log = LoggerFactory.getLogger(LifecycleTransitionLogger.class);
    private final File dotFile;
    private final FileWriter fileWriter;
    @Inject
    private EventBus eventBus;

    public LifecycleTransitionLogger() throws IOException {
        dotFile = File.createTempFile("lifecycle", ".dot");
        fileWriter = new FileWriter(dotFile);
        log.info("Writing to the file: {}", dotFile.getAbsolutePath());

        fileWriter.write("digraph lifecycle {\n");
    }


    @Subscribe
    public void logStateChanged(final LifecycleStateChangedEvent event) {
        log.info("Event {}.", event);

        try {
            fileWriter.write(format("\t%s -> %s [label=\"%s\"];%n", event.getPreviousState(), event.getNewState(),
                    event.getEvent()));
        } catch (final IOException e) {
            log.error("Could not write to the file. (This exception is normal at the end of execution).", e);
        }
    }

    @Override
    public void init() {
        eventBus.register(this);
    }

    @Override
    public boolean finish() {
        try {
            fileWriter.write("}");
            fileWriter.close();
            log.info("DOT file: {}", dotFile.getAbsolutePath());
            return true;
        } catch (final IOException e) {
            log.error("Could not write to the file.", e);
            return false;
        }
    }
}
