package org.hage.platform.component.execution.utils;


import org.junit.Before;
import org.mockito.MockitoAnnotations;


public abstract class BaseTest {

    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }
}
