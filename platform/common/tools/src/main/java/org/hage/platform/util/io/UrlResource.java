package org.hage.platform.util.io;


import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;


public class UrlResource implements Resource {

    private URL url;

    public UrlResource(String uri) throws MalformedURLException {
        this.url = new URL(uri);
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return url.openStream();
    }

    @Override
    public URI getUri() throws URISyntaxException {
        return url.toURI();
    }

}
