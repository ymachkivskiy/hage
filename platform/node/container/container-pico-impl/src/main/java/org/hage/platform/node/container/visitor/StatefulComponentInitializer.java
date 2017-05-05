package org.hage.platform.node.container.visitor;


import org.hage.platform.simulation.container.Stateful;
import org.picocontainer.ComponentAdapter;
import org.picocontainer.ComponentFactory;
import org.picocontainer.Parameter;
import org.picocontainer.PicoContainer;
import org.picocontainer.visitors.AbstractPicoVisitor;


public class StatefulComponentInitializer extends AbstractPicoVisitor {

    @Override
    public boolean visitContainer(final PicoContainer pico) {
        pico.getComponents(Stateful.class);
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
