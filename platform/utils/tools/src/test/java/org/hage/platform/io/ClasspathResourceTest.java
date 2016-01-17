package org.hage.platform.io;


import org.hage.platform.util.io.ClasspathResource;
import org.hage.platform.util.io.UnknownSchemeException;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URISyntaxException;

import static org.junit.Assert.assertNotNull;


public class ClasspathResourceTest {

    private static final String RESOURCE_EXISTING = "classpath:org/hage/util/io/testResource.res";

    private static final String RESOURCE_NOT_EXISTING = "classpath:org/hage/util/io/noResource.res";

    private static final String RESOURCE_INCORRECT_URI = "claspath:org/hage/util/io/testResource.res";


    @Test
    public void testGetInputStream() throws UnknownSchemeException, FileNotFoundException {
        ClasspathResource classpathResource = new ClasspathResource(RESOURCE_EXISTING);
        InputStream inputStream = classpathResource.getInputStream();
        assertNotNull(inputStream);
    }


    @Test
    public void testGetUri() throws URISyntaxException, UnknownSchemeException {
        ClasspathResource classpathResource = new ClasspathResource(RESOURCE_EXISTING);
        assertNotNull(classpathResource.getUri());
    }


    @Test(expected = FileNotFoundException.class)
    public void testNotExisitngResource() throws FileNotFoundException, UnknownSchemeException, URISyntaxException {
        ClasspathResource classpathResource = new ClasspathResource(RESOURCE_NOT_EXISTING);

        assertNotNull(classpathResource.getUri());
        classpathResource.getInputStream(); // Should throw
    }


    @SuppressWarnings("unused")
    @Test(expected = UnknownSchemeException.class)
    public void testIncorrectUriResource() throws UnknownSchemeException {
        ClasspathResource classpathResource = new ClasspathResource(RESOURCE_INCORRECT_URI); // Should throw
    }

}
