package org.jage.configuration.communication;


import org.jage.communication.common.AbstractRemoteServiceChanelEndpoint;


public class ConfigurationRemoteServiceChanelEndpoint
        extends AbstractRemoteServiceChanelEndpoint<ConfigurationMessage> {


    public static final String SERVICE_NAME = "ConfigurationPropagationHub";

    protected ConfigurationRemoteServiceChanelEndpoint() {
        super(SERVICE_NAME);
    }

}
