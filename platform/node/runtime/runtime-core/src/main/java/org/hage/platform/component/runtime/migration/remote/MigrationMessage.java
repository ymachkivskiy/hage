package org.hage.platform.component.runtime.migration.remote;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.hage.platform.component.runtime.migration.internal.InternalMigrationGroup;

import java.io.Serializable;
import java.util.List;

import static java.util.Collections.emptyList;

@Getter
@ToString(doNotUseGetters = true)
@RequiredArgsConstructor
class MigrationMessage implements Serializable {
    private final MessageType messageType;
    private final List<InternalMigrationGroup> migrationGroups;


    public static MigrationMessage ackMsg() {
        return new MigrationMessage(MessageType.ACK, emptyList());
    }

    public static MigrationMessage payloadMsg(List<InternalMigrationGroup> internalMigrationGroups) {
        return new MigrationMessage(MessageType.SEND, internalMigrationGroups);
    }
}
