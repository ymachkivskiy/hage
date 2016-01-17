package org.hage.platform.util.io;


import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;



public class ClasspathResource implements Resource {

    /**
     * URI scheme of classpath resources.
     * <p>
     * Please, note that this contains a following colon.
     */
    public static final String CLASSPATH_SCHEME = "classpath:";

    private static final int SCHEME_LENGTH = CLASSPATH_SCHEME.length();

    private ClassLoader classLoader;

    private String uri;


    public ClasspathResource(String uri) throws UnknownSchemeException {
        if(!uri.startsWith(CLASSPATH_SCHEME)) {
            throw new UnknownSchemeException(String.format("Scheme of %s URI is not known. %s was expected.", uri,
                                                           CLASSPATH_SCHEME));
        }
        this.classLoader = getDefaultClassLoader();
        this.uri = uri;
    }

    public static ClassLoader getDefaultClassLoader() {
        ClassLoader cl = null;
        try {
            cl = Thread.currentThread().getContextClassLoader();
        } catch(SecurityException ex) {
            // Empty
        }
        if(cl == null) {
            cl = ClasspathResource.class.getClassLoader();
        }
        return cl;
    }

    @Override
    public InputStream getInputStream() throws FileNotFoundException {
        InputStream is = classLoader.getResourceAsStream(uri.substring(SCHEME_LENGTH));
        if(is == null) {
            throw new FileNotFoundException(String.format("Requested file %s could not be found.", uri));
        }
        return is;
    }

    @Override
    public URI getUri() throws URISyntaxException {
        return new URI(uri);
    }

}
