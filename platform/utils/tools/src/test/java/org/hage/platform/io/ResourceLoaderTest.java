package org.hage.platform.io;


import org.hage.platform.util.io.ClasspathResource;
import org.hage.platform.util.io.IncorrectUriException;
import org.hage.platform.util.io.ResourceLoader;
import org.hage.platform.util.io.UrlResource;
import org.junit.Test;

import static org.junit.Assert.assertTrue;



public class ResourceLoaderTest {

    private static final String CLASSPATH_RESOURCE = "classpath:org/hage/util/io/testResource.res";

    private static final String FILE_RESOURCE = "file:///this/does/not/exist.s";

    private static final String RELATIVE_RESOURCE = "some/dir/exist.s";

    private static final String INCORRECT_RESOURCE = "incorrect:scheme";

    /**
     * Tests obtaining resources for correct URIs.
     */
    @Test
    public void testCorrectGetResource() throws IncorrectUriException {
        assertTrue(ResourceLoader.getResource(CLASSPATH_RESOURCE) instanceof ClasspathResource);

        assertTrue(ResourceLoader.getResource(FILE_RESOURCE) instanceof UrlResource);

        assertTrue(ResourceLoader.getResource(RELATIVE_RESOURCE) instanceof UrlResource);
    }

    /**
     * Tests obtaining resources for incorrect URIs.
     */
    @Test(expected = IncorrectUriException.class)
    public void testIncorrectGetResource() throws IncorrectUriException {
        ResourceLoader.getResource(INCORRECT_RESOURCE);
    }

    /**
     * Tests obtaining a resource for null URI.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNullGetResource() throws IncorrectUriException {
        ResourceLoader.getResource(null);
    }

    /**
     * Tests obtaining a resource for an empty string.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testEmptyGetResource() throws IncorrectUriException {
        ResourceLoader.getResource("");
    }
}
