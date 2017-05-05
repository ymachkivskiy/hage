package org.hage.platform.node.runtime.container.dependency;

import org.hage.platform.node.container.InstanceContainer;

public interface DependenciesInjector {
    void injectDependenciesUsing(Object object, InstanceContainer container);
}
