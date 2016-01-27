package org.hage.platform.util.container.share;

import lombok.extern.slf4j.Slf4j;
import org.hage.platform.component.provider.IMutableComponentInstanceProvider;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;

@Slf4j
class SharedComponentsPostProcessor implements BeanPostProcessor {


    @Autowired
    protected IMutableComponentInstanceProvider container;


    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if (bean.getClass().getAnnotationsByType(SharedBetweenContainers.class).length > 0) {
          boolean dupa = false;
        }
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
