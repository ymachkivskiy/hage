package org.hage.util.io;


import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URISyntaxException;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;


public class UrlResourceTest {

    private static final String FILE_RESOURCE_EXISTING_REL = "testResource.res";

    private static final String HTTP_RESOURCE_EXISTING = "http://google.com/";

    private static final String RESOURCE_NOT_EXISTING = "file:///this/does/not/exist.s";

    private static final String RESOURCE_INCORRECT_URI = "unknown://google.com/";


    @Test
    public void testGetInputStreamForAbsoluteFile() throws IOException {
        String absoluteFileUri = getClass().getResource(FILE_RESOURCE_EXISTING_REL).toExternalForm();
        UrlResource urlResource = new UrlResource(absoluteFileUri);
        InputStream inputStream = urlResource.getInputStream();
        assertNotNull(inputStream);
    }


    @Test
    public void testGetUriForAbsoluteFile() throws MalformedURLException, URISyntaxException {
        String absoluteFileUri = getClass().getResource(FILE_RESOURCE_EXISTING_REL).toExternalForm();
        UrlResource urlResource = new UrlResource(absoluteFileUri);
        assertNotNull(urlResource.getUri());
    }


    @Test
    @Ignore
    public void testGetInputStreamForHttp() throws IOException {
        UrlResource urlResource = new UrlResource(HTTP_RESOURCE_EXISTING);
        InputStream inputStream = urlResource.getInputStream();
        assertNotNull(inputStream);
    }


    @Test
    public void testGetUriForHttp() throws MalformedURLException, URISyntaxException {
        UrlResource urlResource = new UrlResource(HTTP_RESOURCE_EXISTING);
        assertNotNull(urlResource.getUri());
    }

    @Test(expected = IOException.class)
    public void testNotExisitngResource() throws URISyntaxException, UnknownSchemeException, IOException {
        UrlResource urlResource = new UrlResource(RESOURCE_NOT_EXISTING);

        assertNull(urlResource.getInputStream());
        assertNotNull(urlResource.getUri());
    }


    @SuppressWarnings("unused")
    @Test(expected = MalformedURLException.class)
    public void testIncorrectUriResource() throws MalformedURLException {
        UrlResource urlResource = new UrlResource(RESOURCE_INCORRECT_URI);
    }

}
