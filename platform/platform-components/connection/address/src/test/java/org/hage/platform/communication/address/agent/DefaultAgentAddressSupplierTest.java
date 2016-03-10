package org.hage.platform.communication.address.agent;


import org.hage.platform.communication.address.LocalNodeAddressSupplier;
import org.hage.platform.communication.address.NodeAddress;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;


/**
 * @author AGH AgE Team
 */
@RunWith(MockitoJUnitRunner.class)
public class DefaultAgentAddressSupplierTest {

    @InjectMocks
    private final DefaultAgentAddressSupplier supplier = new DefaultAgentAddressSupplier();
    @InjectMocks
    private final DefaultAgentAddressSupplier templatedSupplier = new DefaultAgentAddressSupplier("template");
    @InjectMocks
    private final DefaultAgentAddressSupplier wildcardedSupplier = new DefaultAgentAddressSupplier("*template*");
    @Mock
    private LocalNodeAddressSupplier nodeAddressSupplier;

    @Before
    public void setup() {
        given(nodeAddressSupplier.getLocalAddress()).willReturn(mock(NodeAddress.class));
    }

    @Test
    public void shouldCreateDefaultAgentAddressInstances() {
        // when
        final AgentAddress address = supplier.get();

        // then
        assertThat(address, instanceOf(DefaultAgentAddress.class));
    }

    @Test
    public void shouldUseNodeAddressInstancesFromNodeAddressSupplier() {
        // given
        final NodeAddress nodeAddress = mock(NodeAddress.class);
        given(nodeAddressSupplier.getLocalAddress()).willReturn(nodeAddress);

        // when
        final AgentAddress address = supplier.get();

        // then
        assertThat(address.getNodeAddress(), is(nodeAddress));
    }

    @Test
    public void shouldUseDefaultTemplate() {
        // when
        final AgentAddress address = supplier.get();

        // then
        assertThat(address.getFriendlyName(), is("agent0"));
    }

    @Test
    public void shouldIncrementWildcardInTemplate() {
        // when
        supplier.get();
        final AgentAddress address = supplier.get();

        // then
        assertThat(address.getFriendlyName(), is("agent1"));
    }

    @Test
    public void shouldUseProvidedTemplate() {
        // when
        final AgentAddress address = templatedSupplier.get();

        // then
        assertThat(address.getFriendlyName(), is("template"));
    }

    @Test
    public void shouldReplaceWildcardInTemplate() {
        // when
        final AgentAddress address = wildcardedSupplier.get();

        // then
        assertThat(address.getFriendlyName(), is("0template0"));
    }
}
