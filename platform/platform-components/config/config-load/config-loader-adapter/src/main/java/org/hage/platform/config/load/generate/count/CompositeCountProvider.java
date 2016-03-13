package org.hage.platform.config.load.generate.count;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.NotImplementedException;
import org.hage.platform.config.load.definition.agent.AgentCountData;
import org.hage.platform.config.load.definition.agent.AgentCountMode;
import org.springframework.beans.factory.annotation.Required;

import java.util.EnumMap;
import java.util.Map;

@Slf4j
public class CompositeCountProvider implements AgentCountProvider {

    private static final AgentCountProvider NOT_IMPLEMENTED_PROVIDER = countData -> {
        log.warn("Count provider not implemented for {}" + countData.getAgentCountMode());
        throw new NotImplementedException("Count provider not implemented for " + countData.getAgentCountMode());
    };

    private Map<AgentCountMode, AgentCountProvider> providersMap = new EnumMap<>(AgentCountMode.class);

    @Override
    public Integer getAgentCount(AgentCountData countData) {
        return providersMap.getOrDefault(countData.getAgentCountMode(), NOT_IMPLEMENTED_PROVIDER).getAgentCount(countData);
    }

    private void setCountProviderForMode(AgentCountProvider provider, AgentCountMode mode) {
        log.debug("Set count provider {} for mode {}", provider, mode);
        providersMap.put(mode, provider);
    }

    @Required
    public void setFixedCountProvider(AgentCountProvider fixedCountProvider) {
        setCountProviderForMode(fixedCountProvider, AgentCountMode.FIXED);
    }

    public void setAtLeastCountProvider(AgentCountProvider atLeastCountProvider) {
        setCountProviderForMode(atLeastCountProvider, AgentCountMode.AT_LEAST);
    }

    public void setAtMostCountProvider(AgentCountProvider atMostCountProvider) {
        setCountProviderForMode(atMostCountProvider, AgentCountMode.AT_MOST);
    }

    public void setBetweenCountProvider(AgentCountProvider betweenCountProvider) {
        setCountProviderForMode(betweenCountProvider, AgentCountMode.BETWEEN);
    }

    public void setRandomCountProvider(AgentCountProvider randomCountProvider) {
        setCountProviderForMode(randomCountProvider, AgentCountMode.RANDOM);
    }

}
