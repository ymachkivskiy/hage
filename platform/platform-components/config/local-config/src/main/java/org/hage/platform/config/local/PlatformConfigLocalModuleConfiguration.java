package org.hage.platform.config.local;

import org.hage.platform.config.local.division.ConfigurationSplitter;
import org.hage.platform.config.local.division.SimpleConfigurationSplitter;
import org.hage.platform.config.local.gen.MergingPopulationDistributionMapCreator;
import org.hage.platform.config.local.gen.PopulationDistributionMapCreator;
import org.hage.platform.config.local.gen.count.*;
import org.hage.platform.config.local.gen.select.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackageClasses = PlatformConfigLocalModuleConfiguration.class)
public class PlatformConfigLocalModuleConfiguration {

    @Bean
    public ConfigurationSplitter getConfigurationSplitter() {
        return new SimpleConfigurationSplitter();
    }

    @Bean
    public PopulationDistributionMapCreator getPopulationDistributionMapCreator() {
        MergingPopulationDistributionMapCreator mapCreator = new MergingPopulationDistributionMapCreator();

        mapCreator.setCountProvider(getTranslationAgentCountProvider());
        mapCreator.setPositionsSelector(getTranslationAgentPositionSelector());

        return mapCreator;
    }

    //region Agent position selectors

    @Bean
    public PositionsSelector getTranslationAgentPositionSelector() {
        CompositePositionsSelector compositePositionsSelector = new CompositePositionsSelector();

        compositePositionsSelector.setAllSelector(getAllPositionsSelector());
        compositePositionsSelector.setRandomPositionsFixedNumberSelector(getRandomSelectionFixedNumberSelector());
        compositePositionsSelector.setRandomPositionsRandomNumberSelector(getRandomSelectionRandomNumberSelector());

        return compositePositionsSelector;
    }

    @Bean
    public PositionsSelector getAllPositionsSelector() {
        return new AllPositionsSelector();
    }

    @Bean
    public PositionsSelector getRandomSelectionFixedNumberSelector() {
        return new RandomSelectionFixedNumberSelector();
    }

    @Bean
    public PositionsSelector getRandomSelectionRandomNumberSelector() {
        return new RandomSelectionRandomNumberSelector();
    }

    //endregion

    //region Agent count providers

    @Bean
    public AgentCountProvider getTranslationAgentCountProvider() {
        CompositeCountProvider resultCountProvider = new CompositeCountProvider();

        resultCountProvider.setAtLeastCountProvider(getAtLeastAgentCountProvider());
        resultCountProvider.setAtMostCountProvider(getAtMostAgentCountProvider());
        resultCountProvider.setBetweenCountProvider(getBetweenAgentCountProvider());
        resultCountProvider.setFixedCountProvider(getFixedAgentCountProvider());
        resultCountProvider.setRandomCountProvider(getRandomAgentCountProvider());

        return resultCountProvider;
    }

    @Bean
    public AgentCountProvider getAtLeastAgentCountProvider() {
        return new AtLeastCountProvider();
    }

    @Bean
    public AgentCountProvider getAtMostAgentCountProvider() {
        return new AtMostCountProvider();
    }

    @Bean
    public AgentCountProvider getBetweenAgentCountProvider() {
        return new BetweenAgentCountProvider();
    }

    @Bean
    public AgentCountProvider getFixedAgentCountProvider() {
        return new FixedCountProvider();
    }

    @Bean
    public AgentCountProvider getRandomAgentCountProvider() {
        return new RandomCountProvider();
    }

    //endregion

}
