package org.hage.platform.rate.local.config;

import org.hage.platform.rate.model.ComputationRatingConfig;
import org.springframework.beans.factory.annotation.Autowired;

//@Component
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
        return new RatingSettings(); //TODO
    }

    public RatingSettings getBaseConfig() {
        return new RatingSettings();
    }
}
