package org.hage.platform.component.runtime.container.dependency;

import org.hage.platform.component.container.InstanceContainer;

public interface DependenciesInjector {
    void injectDependenciesUsing(Object object, InstanceContainer container);
}
