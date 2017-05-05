package org.hage.platform.node.runtime.container.dependency;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hage.platform.annotation.di.SingletonComponent;
import org.hage.platform.node.container.InstanceContainer;

import javax.inject.Inject;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;


@SingletonComponent
@Slf4j
class CachedDependenciesEraser implements DependenciesEraser, DependenciesInjector {

    private final Map<Class, ClazzDependenciesWorker> workersCache = new ConcurrentHashMap<>();

    @Override
    public void eraseDependencies(Object object) {
        checkNotNull(object);
        workerFor(object).eraseDependencies(object);
    }

    @Override
    public void injectDependenciesUsing(Object object, InstanceContainer container) {
        checkNotNull(object);
        checkNotNull(container);

        workerFor(object).injectDependenciesUsing(object, container);
    }

    private ClazzDependenciesWorker workerFor(Object object) {
        return workersCache.computeIfAbsent(object.getClass(), ClazzDependenciesWorker::new);
    }

    @RequiredArgsConstructor
    private static class ClazzDependenciesWorker implements DependenciesInjector, DependenciesEraser {

        private final List<Field> fieldsWithDependencies;

        public ClazzDependenciesWorker(Class clazz) {
            fieldsWithDependencies = stream(clazz.getDeclaredFields())
                .filter(f -> f.getAnnotation(Inject.class) != null)
                .peek(f -> f.setAccessible(true))
                .collect(toList());
        }

        @Override
        public void eraseDependencies(Object object) {
            log.trace("Erase dependencies for {}", object);
            for (Field field : fieldsWithDependencies) {
                try {
                    log.trace("Erasing '{}'", field.getName());

                    field.set(object, null);
                } catch (IllegalAccessException e) {
                    log.error("Unable to erase field with dependency {} for {}", field.getName(), object);
                }
            }
        }

        @Override
        public void injectDependenciesUsing(Object object, InstanceContainer container) {
            checkNotNull(container);

            log.trace("Inject dependencies for {}", object);

            for (Field field : fieldsWithDependencies) {

                log.trace("Injecting to '{}' of type {}", field.getName(), field.getType());
                Object dependency = container.getInstance(field.getType());

                if (dependency == null) {
                    log.warn("Dependency for '{}' of type {} not found", field.getName(), field.getType());
                    return;
                }

                try {
                    field.set(object, dependency);
                } catch (IllegalAccessException e) {
                    log.error("Unable to inject to field {} dependency {} for {}", field.getName(), dependency, object);
                }

            }
        }

    }


}
