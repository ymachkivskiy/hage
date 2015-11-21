package org.hage.platform.config.transl;

import org.hage.platform.config.def.HabitatExternalConfiguration;
import org.hage.platform.config.def.HabitatInternalConfiguration;

public interface HabitatConfigurationTranslator {
    HabitatInternalConfiguration toInternalModel(HabitatExternalConfiguration source);
}
