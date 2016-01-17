package org.hage.platform.util.io;


import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;


public class ResourceLoader {

    public static Resource getResource(String uri) throws IncorrectUriException {
        if(uri == null || uri.isEmpty()) {
            throw new IllegalArgumentException("URI cannot be null nor empty.");
        }

        if(uri.startsWith(ClasspathResource.CLASSPATH_SCHEME)) {
            try {
                return new ClasspathResource(uri);
            } catch(UnknownSchemeException e) {
                // If we are here it is very strange.
                assert false;
            }
        }

        try {
            URI parsedUri = new URI(uri);
            if(parsedUri.getScheme() == null) {
                return new UrlResource("file:" + uri);
            }
            return new UrlResource(uri);

        } catch(MalformedURLException e) {
            throw new IncorrectUriException(e);
        } catch(URISyntaxException e) {
            throw new IncorrectUriException(e);
        }
    }
}
