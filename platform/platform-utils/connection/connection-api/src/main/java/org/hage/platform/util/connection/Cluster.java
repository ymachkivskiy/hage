package org.hage.platform.util.connection;

import org.hage.platform.util.connection.chanel.ChanelDescriptor;
import org.hage.platform.util.connection.chanel.FrameChanel;
import org.hage.platform.util.connection.member.MembershipManager;

public interface Cluster {
    FrameChanel getChanel(ChanelDescriptor chanelDescriptor);

    MembershipManager getMembersManager();
}
