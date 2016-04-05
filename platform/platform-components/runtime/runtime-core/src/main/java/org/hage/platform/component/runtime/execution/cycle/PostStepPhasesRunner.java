package org.hage.platform.component.runtime.execution.cycle;

import lombok.extern.slf4j.Slf4j;
import org.hage.platform.component.runtime.execution.ExecutionStepAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.hage.util.CollectionUtils.nullSafe;

@Component
@Slf4j
public class PostStepPhasesRunner {

    @Autowired(required = false)
    private List<ExecutionStepAware> stepAwareList;
    @Autowired
    private PostStepPhaseOrderClassifier classifier;

    public void stepPerformed(long stepNumber) {
        stepAwareList.forEach(stepAware -> stepAware.onStepPerformed(stepNumber));
    }

    @PostConstruct
    private void sortStepAwareList() {
        stepAwareList = nullSafe(stepAwareList)
            .stream()
            .filter(stepAware -> classifier.isCorrectPhase(stepAware.getPhase()))
            .sorted((p1, p2) -> classifier.compare(p1.getPhase(), p2.getPhase()))
            .collect(toList());

        log.debug("Post step aware ordered list {}", stepAwareList);
    }
}
