package org.hage.platform.config.distribution.endpoint.message;

import org.hage.platform.config.ComputationConfiguration;

import static org.hage.platform.config.distribution.endpoint.message.MessageType.*;

public class MessageUtils {

    public static ConfigurationMessage checkNeedConfigMsg() {
        return new ConfigurationMessage(CHECK_WILL_ACCEPT_CONFIG, null);
    }

    public static boolean isCheckMsg(ConfigurationMessage message) {
        return message.getMessageType() == CHECK_WILL_ACCEPT_CONFIG;
    }

    public static ConfigurationMessage requestConfigMsg() {
        return new ConfigurationMessage(REQUEST_FOR_CONFIG, null);
    }

    public static boolean isRequestMsg(ConfigurationMessage message) {
        return message.getMessageType() == REQUEST_FOR_CONFIG;
    }

    public static ConfigurationMessage refuseMsg() {
        return new ConfigurationMessage(REFUSE_TO_ACCEPT_CONFIG, null);
    }

    public static ConfigurationMessage distributeMsg(ComputationConfiguration configuration) {
        return new ConfigurationMessage(DISTRIBUTE_CONFIG, configuration);
    }

    public static boolean isDistributeMsg(ConfigurationMessage message) {
        return message.getMessageType() == DISTRIBUTE_CONFIG;
    }

}
