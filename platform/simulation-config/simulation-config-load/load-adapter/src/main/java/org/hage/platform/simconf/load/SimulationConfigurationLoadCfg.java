package org.hage.platform.simconf.load;

import org.hage.platform.annotation.di.PlugableConfiguration;
import org.hage.platform.simconf.load.generate.MergingPopulationDistributionMapCreator;
import org.hage.platform.simconf.load.generate.PopulationDistributionMapCreator;
import org.hage.platform.simconf.load.generate.count.*;
import org.hage.platform.simconf.load.generate.select.*;
import org.springframework.context.annotation.Bean;

@PlugableConfiguration
public class SimulationConfigurationLoadCfg {

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
