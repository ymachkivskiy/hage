package org.hage.platform.util.bus;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;

@Slf4j
public class EventBusRegisterPostProcessor implements BeanPostProcessor {

    @Autowired
    private EventBus eventBus;

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        log.debug("Checking bean bean {} ({}) for event bus subscription", bean, beanName);

        if (bean instanceof EventSubscriber) {
            log.debug("Registering bean {} ({}) with eventbus subscriber", bean, beanName);
            eventBus.register((EventSubscriber) bean);
        }

        return bean;
    }
}
