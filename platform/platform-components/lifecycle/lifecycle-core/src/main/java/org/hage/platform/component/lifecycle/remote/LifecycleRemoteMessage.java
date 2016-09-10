package org.hage.platform.component.lifecycle.remote;


import lombok.Data;
import org.hage.platform.component.lifecycle.BaseLifecycleCommand;

import java.io.Serializable;


@Data
class LifecycleRemoteMessage implements Serializable {
    private final BaseLifecycleCommand command;
}
