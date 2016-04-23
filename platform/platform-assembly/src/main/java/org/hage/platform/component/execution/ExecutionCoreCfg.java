package org.hage.platform.component.execution;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(StepPhaseOrderCfg.class)
public class ExecutionCoreCfg {
}
