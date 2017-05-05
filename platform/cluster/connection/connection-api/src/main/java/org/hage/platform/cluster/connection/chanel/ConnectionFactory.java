package org.hage.platform.cluster.connection.chanel;

public interface ConnectionFactory {
    FrameSender senderFor(ConnectionDescriptor descriptor);

    FrameReceiverAdapter receiverAdapterFor(ConnectionDescriptor descriptor);
}
