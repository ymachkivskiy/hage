package org.hage.platform.containermerge;


import org.hage.platform.component.provider.IMutableComponentInstanceProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;

import java.util.List;


public class ContainerComponentShare {

    @Autowired
    protected IMutableComponentInstanceProvider container;

    protected List<Object> componentsForSharing;

    @Required
    public void setComponentsForSharing(List<Object> componentsForSharing) {
        this.componentsForSharing = componentsForSharing;
    }

    public void shareComponents() {
        componentsForSharing.forEach(container::addComponentInstance);
    }

}
