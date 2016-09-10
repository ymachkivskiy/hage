package org.hage.platform.util.executors.config.external;

import org.hage.platform.config.ConfigurationItem;
import org.hage.platform.config.ConfigurationItemProperties;
import org.hage.platform.config.ConfigurationValueCheckException;
import org.hage.platform.util.executors.config.internal.ThreadingPolicyConfiguration;

import java.util.Objects;
import java.util.regex.Matcher;

import static java.util.regex.Pattern.compile;
import static org.hage.platform.util.executors.config.internal.ThreadingPolicyType.*;

class ThreadingPolicyConfigurationItem extends ConfigurationItem {

    private static final String ADJUSTED_POLICY_NAME = "adjusted";
    private static final String DIRECT_POLICY_NAME = "direct";
    private static final String FIXED_POLICY_PATTERN = "fixed(\\d+)";

    protected ThreadingPolicyConfigurationItem() {
        super(new ConfigurationItemProperties(
            false,
            "tp",
            "threading-policy",
            "Specifies threading policy of this node. Possible values: direct - execution in one single thread, adjusted - execution in same number of threads as there are available, fixedN - execution in N threads, where N is integer number",
            true,
            String.class,
            "POLICY",
            ADJUSTED_POLICY_NAME
        ));
    }

    public ThreadingPolicyConfiguration convert(String policy) {

        if (ADJUSTED_POLICY_NAME.equals(policy)) {
            return new ThreadingPolicyConfiguration(ADJUSTED_TO_PROCESSORS_NUMBER, null);
        } else if (DIRECT_POLICY_NAME.equals(policy)) {
            return new ThreadingPolicyConfiguration(DIRECT_EXECUTION, null);
        }

        Matcher matcher = compile(FIXED_POLICY_PATTERN).matcher(policy);

        if(matcher.find()) {
            int threadsNumber = Integer.parseInt(matcher.group(1));
            return new ThreadingPolicyConfiguration(FIXED_THREADS_NUMBER, threadsNumber);
        }

        return null;
    }

    @Override
    public void checkValue(Object defaultValue) throws ConfigurationValueCheckException {
        if (!(defaultValue instanceof String) || !compile(ADJUSTED_POLICY_NAME + "|" + DIRECT_POLICY_NAME + "|" + FIXED_POLICY_PATTERN).matcher(((String) defaultValue)).matches()) {
            throw new ConfigurationValueCheckException("Threading policy " + Objects.toString(defaultValue) + " is not correct");
        }
    }
}
