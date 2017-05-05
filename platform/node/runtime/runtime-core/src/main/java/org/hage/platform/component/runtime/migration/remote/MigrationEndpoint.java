package org.hage.platform.component.runtime.migration.remote;

import lombok.extern.slf4j.Slf4j;
import org.hage.platform.annotation.di.SingletonComponent;
import org.hage.platform.component.runtime.migration.InputMigrationQueue;
import org.hage.platform.component.runtime.migration.external.ExternalMigrationGroup;
import org.hage.platform.cluster.connection.chanel.ConnectionDescriptor;
import org.hage.platform.cluster.connection.remote.endpoint.BaseRemoteEndpoint;
import org.hage.platform.cluster.connection.remote.endpoint.MessageEnvelope;
import org.springframework.beans.factory.annotation.Autowired;

import static org.hage.platform.component.runtime.migration.remote.MigrationMessage.ackMsg;
import static org.hage.platform.component.runtime.migration.remote.MigrationMessage.payloadMsg;

@SingletonComponent
@Slf4j
public class MigrationEndpoint extends BaseRemoteEndpoint<MigrationMessage> implements ExternalMigrationSender {

    private static final String CHANEL_NAME = "migration-remote-chanel";

    @Autowired
    private InputMigrationQueue inputMigrationQueue;

    protected MigrationEndpoint() {
        super(new ConnectionDescriptor(CHANEL_NAME), MigrationMessage.class);
    }

    @Override
    public void sendMigrants(ExternalMigrationGroup migrationGroup) {
        log.debug("Send migration group {}", migrationGroup);

        sendToAndWaitForResponse(payloadMsg(migrationGroup.getMigrationGroups()), migrationGroup.getTargetNode());
    }

    @Override
    protected MigrationMessage consumeMessageAndRespond(MessageEnvelope<MigrationMessage> envelope) {
        log.debug("Got migrants {}", envelope);

        inputMigrationQueue.acceptMigrants(envelope.getBody().getMigrationGroups());

        return ackMsg();
    }
}
