package org.hage.platform.util.container.share;

import lombok.extern.slf4j.Slf4j;
import org.hage.platform.component.container.MutableInstanceContainer;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;

@Slf4j
class SharedComponentsPostProcessor implements BeanPostProcessor {


    @Autowired
    protected MutableInstanceContainer container;


    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        log.debug("Checking bean {} if it is sharable across containers.", beanName);

        if (bean.getClass().getAnnotationsByType(SharedBetweenContainers.class).length > 0) {
            log.debug("Adding bean {} as shared to container", beanName);

            container.addComponentInstance(bean);
        }

        return bean;
    }
}
