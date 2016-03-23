package org.hage.platform.component.container.visitor;

import org.hage.platform.component.container.exception.ComponentException;
import org.hage.platform.simulation.container.Stateful;
import org.picocontainer.ComponentAdapter;
import org.picocontainer.ComponentFactory;
import org.picocontainer.Parameter;
import org.picocontainer.PicoContainer;
import org.picocontainer.behaviors.Cached;
import org.picocontainer.visitors.AbstractPicoVisitor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class StatefulComponentFinisher extends AbstractPicoVisitor {

    private static final Logger log = LoggerFactory.getLogger(StatefulComponentFinisher.class);

    @Override
    public boolean visitContainer(final PicoContainer pico) {
        for(ComponentAdapter<Stateful> adapter : pico.getComponentAdapters(Stateful.class)) {
            if(adapter.findAdapterOfType(Cached.class) != null) {
                try {
                    adapter.getComponentInstance(pico, ComponentAdapter.NOTHING.class).finish();
                } catch(final ComponentException e) {
                    log.error("Exception during component finish.", e);
                }
            }
        }
        return true;
    }

    @Override
    public void visitComponentAdapter(final ComponentAdapter<?> componentAdapter) {
    }

    @Override
    public void visitComponentFactory(final ComponentFactory componentFactory) {
    }

    @Override
    public void visitParameter(final Parameter parameter) {
    }
}
