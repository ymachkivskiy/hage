package org.hage.platform.util.io;


import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;


public interface Resource {


    InputStream getInputStream() throws IOException;


    URI getUri() throws URISyntaxException;
}
