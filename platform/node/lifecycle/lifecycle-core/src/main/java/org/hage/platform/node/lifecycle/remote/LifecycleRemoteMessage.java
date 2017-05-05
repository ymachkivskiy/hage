package org.hage.platform.node.lifecycle.remote;


import lombok.Data;
import org.hage.platform.node.lifecycle.BaseLifecycleCommand;

import java.io.Serializable;


@Data
class LifecycleRemoteMessage implements Serializable {
    private final BaseLifecycleCommand command;
}
