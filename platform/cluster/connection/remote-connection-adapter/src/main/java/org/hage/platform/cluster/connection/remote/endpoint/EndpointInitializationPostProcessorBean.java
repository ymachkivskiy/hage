package org.hage.platform.cluster.connection.remote.endpoint;

import lombok.extern.slf4j.Slf4j;
import org.hage.platform.cluster.api.LocalClusterNode;
import org.hage.platform.cluster.connection.chanel.ConnectionDescriptor;
import org.hage.platform.cluster.connection.chanel.ConnectionFactory;
import org.hage.platform.cluster.connection.chanel.FrameReceiverAdapter;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;

@Slf4j
public class EndpointInitializationPostProcessorBean implements BeanPostProcessor {

    @Autowired
    private ConnectionFactory connectionFactory;
    @Autowired
    private LocalClusterNode localClusterNode;

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {

        if (bean instanceof BaseRemoteEndpoint) {
            BaseRemoteEndpoint endpoint = (BaseRemoteEndpoint) bean;
            processRemoteMessageSender(endpoint);
            processRemoteMessageSubscriber(endpoint);
        }

        return bean;
    }

    private void processRemoteMessageSender(BaseRemoteEndpoint sender) {
        ConnectionDescriptor descriptor = sender.getDescriptor();

        log.debug("Initializing message sender '{}' using connection descriptor '{}'", sender.getClass(), descriptor);

        if (descriptor == null) {
            log.warn("Not specified connection descriptor for remote message sender '{}'", sender.getClass());
        } else {
            sender.setFrameSender(connectionFactory.senderFor(descriptor));
        }
    }

    private void processRemoteMessageSubscriber(BaseRemoteEndpoint subscriber) {
        ConnectionDescriptor descriptor = subscriber.getDescriptor();

        log.debug("Initializing message subscriber '{}' using connection descriptor '{}'", subscriber.getClass(), descriptor);

        if (descriptor == null) {
            log.warn("Not specified connection descriptor for remote message subscriber '{}'", subscriber.getClass());
        } else {

            @SuppressWarnings("unchecked")
            FrameReceiverMessageAdapter forwardAdapter = new FrameReceiverMessageAdapter<>(subscriber, localClusterNode);
            FrameReceiverAdapter frameReceiverAdapter = connectionFactory.receiverAdapterFor(descriptor);

            frameReceiverAdapter.setReceiver(forwardAdapter);
            frameReceiverAdapter.setRespondReceiver(forwardAdapter);
        }

    }


}
