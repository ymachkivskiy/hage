package org.hage.platform.component.simulationconfig.endpoint.message;

import org.hage.platform.component.simulationconfig.Configuration;

public class MessageUtils {

    public static ConfigurationMessage checkNeedConfigMsg() {
        return new ConfigurationMessage(MessageType.CHECK_WILL_ACCEPT_CONFIG, null);
    }

    public static boolean isCheckMsg(ConfigurationMessage message) {
        return message.getMessageType() == MessageType.CHECK_WILL_ACCEPT_CONFIG;
    }

    public static ConfigurationMessage requestConfigMsg() {
        return new ConfigurationMessage(MessageType.REQUEST_FOR_CONFIG, null);
    }

    public static boolean isRequestMsg(ConfigurationMessage message) {
        return message.getMessageType() == MessageType.REQUEST_FOR_CONFIG;
    }

    public static ConfigurationMessage refuseMsg() {
        return new ConfigurationMessage(MessageType.REFUSE_TO_ACCEPT_CONFIG, null);
    }

    public static ConfigurationMessage distributeMsg(Configuration configuration) {
        return new ConfigurationMessage(MessageType.DISTRIBUTE_CONFIG, configuration);
    }

    public static boolean isDistributeMsg(ConfigurationMessage message) {
        return message.getMessageType() == MessageType.DISTRIBUTE_CONFIG;
    }

}
