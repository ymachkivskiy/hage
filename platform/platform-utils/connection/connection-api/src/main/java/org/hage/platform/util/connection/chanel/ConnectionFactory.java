package org.hage.platform.util.connection.chanel;

public interface ConnectionFactory {
    FrameSender senderFor(ConnectionDescriptor descriptor);

    FrameReceiverAdapter receiverAdapterFor(ConnectionDescriptor descriptor);
}
