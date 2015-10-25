package org.jage.workplace.manager;


import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;
import com.google.common.eventbus.Subscribe;
import com.google.common.util.concurrent.ListeningScheduledExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.hazelcast.core.IMap;
import lombok.extern.slf4j.Slf4j;
import org.jage.address.agent.AgentAddress;
import org.jage.address.node.NodeAddress;
import org.jage.address.node.NodeAddressSupplier;
import org.jage.address.selector.AddressSelector;
import org.jage.address.selector.Selectors;
import org.jage.agent.IAgent;
import org.jage.agent.ISimpleAgent;
import org.jage.bus.EventBus;
import org.jage.communication.api.RemoteChannel;
import org.jage.communication.api.RemoteCommunicationManager;
import org.jage.communication.api.RemoteMessageSubscriber;
import org.jage.communication.message.Message;
import org.jage.configuration.event.ConfigurationUpdatedEvent;
import org.jage.platform.component.definition.IComponentDefinition;
import org.jage.platform.component.exception.ComponentException;
import org.jage.platform.component.pico.IPicoComponentInstanceProvider;
import org.jage.platform.component.pico.PicoComponentInstanceProvider;
import org.jage.query.AgentEnvironmentQuery;
import org.jage.query.IQuery;
import org.jage.services.core.CoreComponentEvent;
import org.jage.util.Locks;
import org.jage.workplace.IStopCondition;
import org.jage.workplace.Workplace;
import org.jage.workplace.WorkplaceException;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.GuardedBy;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;

import static com.google.common.base.Objects.toStringHelper;
import static com.google.common.base.Preconditions.checkState;
import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Lists.newLinkedList;
import static com.google.common.collect.Maps.newHashMap;
import static com.google.common.collect.Sets.newHashSet;
import static java.util.Objects.requireNonNull;


/**
 * Default implementation of {@link WorkplaceManager}.
 * <p>
 *
 * @author AGH AgE Team
 */
@ParametersAreNonnullByDefault
@Slf4j
public class DefaultWorkplaceManager implements WorkplaceManager,
                                                RemoteMessageSubscriber<WorkplaceManagerMessage> {

    public static final String QUERY_CACHE_NAME = "queryCache";
    public static final String WORKPLACES_MAP_NAME = "workplaces";

    @GuardedBy("workplacesLock")
    @Nonnull
    private final Map<AgentAddress, Workplace> initializedWorkplaces = newHashMap();
    @Nonnull
    private final List<Workplace> activeWorkplaces = newLinkedList();
    @Nonnull
    private final ReadWriteLock workplacesLock = new ReentrantReadWriteLock(true);
    @Nonnull
    private final ListeningScheduledExecutorService executorService = MoreExecutors.listeningDecorator(
            Executors.newSingleThreadScheduledExecutor(
                    (new ThreadFactoryBuilder()).setNameFormat("wp-mgr-%d").build()));
    @Autowired
    protected PicoComponentInstanceProvider instanceProvider;
    @Nullable
    private List<Workplace> configuredWorkplaces;
    private IMap<NodeAddress, Set<AgentAddress>> workplacesMap;
    private IMap<AgentAddress, Map<String, Iterable<?>>> queryCache;
    @Autowired
    private RemoteCommunicationManager communicationManager;
    private RemoteChannel<WorkplaceManagerMessage> communicationChannel;
    @Autowired
    private NodeAddressSupplier nodeAddressProvider;
    private IPicoComponentInstanceProvider childContainer;
    @Autowired
    private EventBus eventBus;

    // Lifecycle methods

    @Override
    public void init() {
        communicationChannel = communicationManager.getCommunicationChannelForService(SERVICE_NAME);
        communicationChannel.subscribeChannel(this);
        eventBus.register(this);
        workplacesMap = communicationManager.getDistributedMap(WORKPLACES_MAP_NAME);
        queryCache = communicationManager.getDistributedMap(QUERY_CACHE_NAME);
    }

    @Override
    public boolean finish() {
        withReadLock(() -> {
            for(final Workplace workplace : initializedWorkplaces.values()) {
                synchronized(workplace) {
                    if(workplace.isStopped()) {
                        workplace.terminate();
                        log.info("Workplace {} finished", workplace);
                    } else {
                        log.error("Cannot terminate still running workplace: {}.", workplace);
                    }
                }
            }
        });
        executorService.shutdown();
        return true;
    }

    protected final void withReadLock(final Runnable action) {
        withReadLockAndRuntimeExceptions(Executors.callable(action));
    }

    protected final <V> V withReadLockAndRuntimeExceptions(final Callable<V> action) {
        return Locks.withReadLockAndRuntimeExceptions(workplacesLock, action);
    }

    @Override
    public void start() {
        checkState(configuredWorkplaces != null, "There are no workplaces configured.");
        initializeWorkplaces();

        childContainer.getInstance(IStopCondition.class);

        // notify all listeners
        eventBus.post(new CoreComponentEvent(CoreComponentEvent.Type.STARTING));

        // start all workplaces
        withReadLock(() -> {
            checkState(!initializedWorkplaces.isEmpty(), "There is no workplace to run.");

            for(final Workplace workplace : initializedWorkplaces.values()) {
                workplace.start();
                activeWorkplaces.add(workplace);
                log.info("Workplace {} started.", workplace);
            }
            updateWorkplacesCache();
        });
    }

    @Override
    public void pause() {
        log.debug("Workplace manager is pausing computation.");
        withReadLock(() -> {
            for(final Workplace workplace : initializedWorkplaces.values()) {
                synchronized(workplace) {
                    if(workplace.isRunning()) {
                        workplace.pause();
                    } else {
                        log.warn("Trying to pause not running workplace: {}.", workplace);
                    }
                }
            }
        });
    }

	/* Workplace management methods */

    @Override
    public void resume() {
        log.debug("Workplace manager is resuming computation.");
        withReadLock(() -> {
            for(final Workplace workplace : initializedWorkplaces.values()) {
                synchronized(workplace) {
                    if(workplace.isPaused()) {
                        workplace.resume();
                    } else {
                        log.warn("Trying to resume not paused workplace: {}.", workplace);
                    }
                }
            }
        });
    }

    @Override
    public void stop() {
        log.debug("Workplace manager is stopping.");
        withReadLock(() -> {
            for(final Workplace workplace : initializedWorkplaces.values()) {
                synchronized(workplace) {
                    if(workplace.isRunning() || workplace.isPaused()) {
                        workplace.stop();
                    } else {
                        log.warn("Trying to stop not running workplace: {}.", workplace);
                    }
                }
            }
            updateWorkplacesCache();
        });
        log.debug("Workplace manager stopped.");
    }

    @Override
    public void teardownConfiguration() {
        checkState(childContainer != null, "No configuration to destroy.");
        checkState(!isActive(), "Cannot destroy configuration of an active manager.");

        instanceProvider.removeChildContainer(childContainer);
        configuredWorkplaces = null;
    }

    /**
     * Indicates if the workplace manager is active, i.e. it contains at least on active workplace.
     *
     * @return true if the manager is active
     */
    public boolean isActive() {
        return !activeWorkplaces.isEmpty();
    }

	/* Workplace environment methods. */

    private void initializeWorkplaces() {
        if(configuredWorkplaces == null || configuredWorkplaces.isEmpty()) {
            throw new ComponentException("There is no workplace defined. Cannot run the computation.");
        }

        log.info("Created {} workplace(s).", configuredWorkplaces.size());

        withWriteLock(() -> {
            for(final Workplace workplace : configuredWorkplaces) {
                final AgentAddress agentAddress = workplace.getAddress();

                // We call setEnvironment() relying on the fact, that all agents and workplaces were
                // initialized before the workplace manager by the container.
                try {
                    initializedWorkplaces.put(agentAddress, workplace);
                    workplace.setEnvironment(DefaultWorkplaceManager.this);
                    log.info("Workplace {} initialized.", agentAddress);
                } catch(final WorkplaceException e) {
                    initializedWorkplaces.remove(
                            agentAddress); // Do not leave not configured workplace in the manager
                    throw new ComponentException(
                            String.format("Cannot set workplace environment for: %s.", agentAddress), e);
                }
            }
        });
    }

    private void updateWorkplacesCache() {
        log.debug("Updating workplaces in global map {} - {}.", nodeAddressProvider.get(),
                  getAddressesOfLocalWorkplaces());
        workplacesMap.put(nodeAddressProvider.get(), getAddressesOfLocalWorkplaces());
    }

    protected final void withWriteLock(final Runnable action) {
        withWriteLockAndRuntimeExceptions(Executors.callable(action));
    }

    @Nonnull
    protected Set<AgentAddress> getAddressesOfLocalWorkplaces() {
        return getLocalWorkplaces().stream()
                .map(Workplace::getAddress)
                .collect(Collectors.toSet());
    }

    protected final <V> V withWriteLockAndRuntimeExceptions(final Callable<V> action) {
        assert (action != null);
        return Locks.withWriteLockAndRuntimeExceptions(workplacesLock, action);
    }

    protected final <V, E extends Exception> V withWriteLockThrowing(final Callable<V> action,
            final Class<E> exceptionClass) throws E {
        assert (action != null && exceptionClass != null);
        return Locks.withWriteLockThrowing(workplacesLock, action, exceptionClass);
    }

    protected final <V, E extends Exception> V withReadLockThrowing(final Callable<V> action,
            final Class<E> exceptionClass) throws E {
        assert (action != null && exceptionClass != null);
        return Locks.withReadLockThrowing(workplacesLock, action, exceptionClass);
    }


    @Override
    public void onWorkplaceStop(@Nonnull final Workplace workplace) {
        log.debug("Get stopped notification from {}", workplace.getAddress());
        if(activeWorkplaces.contains(workplace)) {
            activeWorkplaces.remove(workplace);
        } else {
            log.error("Received event notify stopped from workplace {} which is already stopped",
                      workplace.getAddress());
        }

        if(activeWorkplaces.isEmpty()) {
            eventBus.post(new CoreComponentEvent(CoreComponentEvent.Type.STOPPED));
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    @Nonnull
    public <E extends IAgent, T> Collection<T> queryWorkplaces(final AgentEnvironmentQuery<E, T> query) {
        final Set<Object> results = newHashSet();
        for(final Map<String, Iterable<?>> value : queryCache.values()) {
            Iterables.addAll(results, value.get(query.getClass().getCanonicalName()));
        }
        log.debug("Global query {} -> {}.", query, results);
        log.debug("Map: {}.", queryCache.entrySet());
        return (Collection<T>) results;
    }

    @Override
    public void sendMessage(@Nonnull final WorkplaceManagerMessage message) {
        requireNonNull(message);
        log.debug("Sending message {}.", message);
        communicationChannel.sendMessageToAll(message);
    }

    @Override
    public void requestRemoval(@Nonnull final AgentAddress agentAddress) {
        // TODO
    }

	/* Distribution-awareness methods */

    @Nonnull
    @Override
    public Set<AgentAddress> getAddressesOfWorkplaces() {
        final ImmutableSet.Builder<AgentAddress> builder = ImmutableSet.builder();
        workplacesMap.values().forEach(builder::addAll);
        return builder.build();
    }

    @Override
    public void cacheQueryResults(@Nonnull final AgentAddress address, @Nonnull final IQuery<?, ?> query,
            final Iterable<?> results) {

        log.debug("Caching {} for {} with {}.", query, address, results);
        try {
            queryCache.lock(address);
            log.debug("{} lock acquired.", address);
            if(!queryCache.containsKey(address)) {
                queryCache.set(address, Maps.<String, Iterable<?>> newHashMap());
            }
            final Map<String, Iterable<?>> map = queryCache.get(address);
            assert map != null;
            map.put(query.getClass().getCanonicalName(), results);
            queryCache.set(address, map);
        } finally {
            log.debug("*Map: {}.", queryCache.entrySet());
            queryCache.unlock(address);
            log.debug("{} lock released.", address);
        }
    }

	/* Lock utilities. */

    /**
     * Attaches a given workplace to this manager.
     *
     * @param workplace workplace to attache
     * @throws WorkplaceException when a given workplace is already attached to this manager or workplace's address exists already in
     *                            the manager
     */
    public void addWorkplace(final Workplace workplace) {
        withWriteLock(() -> {
            final AgentAddress agentAddress = workplace.getAddress();
            if(!initializedWorkplaces.containsKey(agentAddress)) {
                initializedWorkplaces.put(agentAddress, workplace);
                if(workplace.isRunning() && !activeWorkplaces.contains(workplace)) {
                    workplace.setEnvironment(DefaultWorkplaceManager.this);
                    activeWorkplaces.add(workplace);
                }
                log.info("Workplace added: {}", workplace.getAddress());
                updateWorkplacesCache();
            } else {
                throw new WorkplaceException(String.format("%s already exists in the manager.", agentAddress));
            }
        });
    }

    @Override
    @Nullable
    public Workplace getLocalWorkplace(@Nonnull final AgentAddress workplaceAddress) {
        return withReadLockAndRuntimeExceptions(() -> initializedWorkplaces.get(workplaceAddress));
    }

    @Override
    @Nonnull
    public List<Workplace> getLocalWorkplaces() {
        return withReadLockAndRuntimeExceptions(() -> ImmutableList.copyOf(initializedWorkplaces.values()));
    }

    private void deliverMessageToWorkplace(@Nonnull final Message<AgentAddress, ?> message) {
        withReadLock(() -> {
            final AddressSelector<AgentAddress> receiverSelector = message.getHeader().getReceiverSelector();
            final Set<AgentAddress> addresses = Selectors.filter(getAddressesOfLocalWorkplaces(), receiverSelector);
            for(final AgentAddress agentAddress : addresses) {
                log.debug("Delivering to {}.", agentAddress);
                getLocalWorkplace(agentAddress).deliverMessage(message);
            }
        });
    }

    @Subscribe
    public void onConfigurationUpdated(@Nonnull final ConfigurationUpdatedEvent event) {
        log.debug("Event: {}.", event);
        checkState(configuredWorkplaces == null, "The core component is already configured.");

        final Collection<IComponentDefinition> componentDefinitions = event.getComponents();

        childContainer = instanceProvider.makeChildContainer();
        for(final IComponentDefinition def : componentDefinitions) {
            childContainer.addComponent(def);
        }
        childContainer.verify();

        configuredWorkplaces = newArrayList(childContainer.getInstances(Workplace.class));
//		childContainer.getInstances(IStopCondition.class);

        log.info("Configured workplaces: {}.", configuredWorkplaces);
        eventBus.post(new CoreComponentEvent(CoreComponentEvent.Type.CONFIGURED));
    }

    @Override
    public void onRemoteMessage(@Nonnull final WorkplaceManagerMessage message) {
        final Serializable payload = message.getPayload();
        switch(message.getType()) {
            case AGENT_MESSAGE:
                if(!(payload instanceof Message)) {
                    log.error("Unrecognizable payload {}.", payload);
                    return;
                }
                final Message<AgentAddress, Serializable> agentMessage = (Message<AgentAddress, Serializable>) payload;
                deliverMessageToWorkplace(agentMessage);
                break;

            case MIGRATE_AGENT:
                if(!(payload instanceof Map)) {
                    log.error("Unrecognizable payload {}.", payload);
                    return;
                }
                final Map<String, Serializable> migrationData = (Map<String, Serializable>) payload;
                final AddressSelector<AgentAddress> targetSelector =
                        (AddressSelector<AgentAddress>) migrationData.get("target");
                final ISimpleAgent agent = (ISimpleAgent) migrationData.get("agent");
                final Set<AgentAddress> addresses = Selectors.filter(getAddressesOfLocalWorkplaces(), targetSelector);
                if(addresses.size() == 1) {//TODO : strange construction
                    getLocalWorkplace(addresses.iterator().next()).sendAgent(agent);
                }
                break;
        }

    }

    @Override
    public String toString() {
        return toStringHelper(this).toString();
    }
}
