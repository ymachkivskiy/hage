package org.hage.platform.config.habitat;

import lombok.Getter;

import java.util.*;

import static java.util.Collections.unmodifiableList;


public final class PopulationDistribution {

    @Getter
    private final List<PopulationQualifier> distribution;

    private PopulationDistribution(Collection<PopulationQualifier> distribution) {
        this.distribution = unmodifiableList(new ArrayList<>(distribution));
    }

    public static class Builder {
        private Set<PopulationQualifier> qualifiers = new HashSet<>();

        public Builder add(PopulationQualifier qualifier) {
            qualifiers.add(qualifier);
            return this;
        }

        public Builder add(Collection<PopulationQualifier> qualifiers) {
            this.qualifiers.addAll(qualifiers);
            return this;
        }

        public PopulationDistribution build() {
            return new PopulationDistribution(qualifiers);
        }
    }

}
