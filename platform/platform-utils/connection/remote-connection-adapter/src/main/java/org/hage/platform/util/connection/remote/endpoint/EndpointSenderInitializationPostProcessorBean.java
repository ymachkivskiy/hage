package org.hage.platform.util.connection.remote.endpoint;

import lombok.extern.slf4j.Slf4j;
import org.hage.platform.util.connection.chanel.ConnectionDescriptor;
import org.hage.platform.util.connection.chanel.ConnectionFactory;
import org.hage.platform.util.connection.chanel.FrameReceiverAdapter;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;

@Slf4j
class EndpointSenderInitializationPostProcessorBean implements BeanPostProcessor {

    @Autowired
    private ConnectionFactory connectionFactory;

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {

        if (bean instanceof BaseRemoteMessageSender) {
            processRemoteMessageSender((BaseRemoteMessageSender) bean);
        } else if (bean instanceof BaseRemoteMessageSubscriber) {
            processRemoteMessageSubscriber((BaseRemoteMessageSubscriber) bean);
        }

        return bean;
    }

    private void processRemoteMessageSender(BaseRemoteMessageSender sender) {
        ConnectionDescriptor descriptor = sender.describe();

        log.debug("Initializing message sender '{}' using connection descriptor '{}'", sender.getClass(), descriptor);

        if (descriptor == null) {
            log.warn("Not specified connection descriptor for remote message sender '{}'", sender.getClass());
        } else {
            sender.setFrameSender(connectionFactory.senderFor(descriptor));
        }
    }

    private void processRemoteMessageSubscriber(BaseRemoteMessageSubscriber subscriber) {
        ConnectionDescriptor descriptor = subscriber.describe();

        log.debug("Initializing message subscriber '{}' using connection descriptor '{}'", subscriber.getClass(), descriptor);

        if (descriptor == null) {
            log.warn("Not specified connection descriptor for remote message subscriber '{}'", subscriber.getClass());
        } else {

            @SuppressWarnings("unchecked")
            FrameReceiverMessageTranslationAdapter forwardAdapter = new FrameReceiverMessageTranslationAdapter<>(subscriber);
            FrameReceiverAdapter frameReceiverAdapter = connectionFactory.receiverAdapterFor(descriptor);

            frameReceiverAdapter.setReceiver(forwardAdapter);
            frameReceiverAdapter.setRespondReceiver(forwardAdapter);
        }

    }


}
