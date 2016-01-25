package org.hage.platform.rate.local.config;

import org.hage.platform.rate.model.ComputationRatingConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
class RatingSettingsProvider {

    @Autowired
    private Settings settings;
        /*

      public ComputationRateConfig getConfigurationForMeasurer(PerformanceMeasurer measurer) { //TODO
          ComputationRateConfig config = categoryConfigurations.get(measurer.getClass());

          if (config == null) {
              config = DEFAULT_CONFIG;
          }

          return config;
      }

      public GlobalRateSettings getGlobalRateSettings() {
          return new GlobalRateSettings(BigInteger.valueOf(GLOBAL_MAX_CATEGORY_RATE)); //TODO

      }
  */

    public RatingSettings getUpdatedConfig(ComputationRatingConfig simulationConfiguration) {
        boolean d = false;
        return null; //TODO
    }

    public RatingSettings getBaseConfig() {
        return null;
    }
}
