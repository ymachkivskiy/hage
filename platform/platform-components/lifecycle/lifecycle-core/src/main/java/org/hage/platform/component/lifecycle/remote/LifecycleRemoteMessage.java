package org.hage.platform.component.lifecycle.remote;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.hage.platform.component.lifecycle.BaseLifecycleCommand;

import javax.annotation.concurrent.Immutable;
import java.io.Serializable;


@RequiredArgsConstructor
@Immutable
public class LifecycleRemoteMessage implements Serializable {
    @Getter
    private final BaseLifecycleCommand command;
}
