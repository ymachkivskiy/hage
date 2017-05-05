package org.hage.platform.node.container.definition;


import org.junit.Before;
import org.junit.Test;


public class SingletonTest {

    @Before
    public void setUp() throws Exception {

    }

    @Test
    public void testSingletonFromConfig() throws Exception {
        // test if singletons injected from the container are
        // really singletons
    }

    @Test
    public void testSingletonProviderAware() throws Exception {
        // test if IComponentProviderAware objects get real singletons
        // when using getInstance()
    }

}
