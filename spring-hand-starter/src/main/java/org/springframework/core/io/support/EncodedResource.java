package org.springframework.core.io.support;

import org.springframework.core.io.InputStreamSource;
import org.springframework.core.io.Resource;

import java.nio.charset.Charset;

/**
 *
 */
public class EncodedResource implements InputStreamSource {
    private final Resource resource;


    private final String encoding;


    private final Charset charset;


    /**
     * Create a new {@code EncodedResource} for the given {@code Resource},
     * not specifying an explicit encoding or {@code Charset}.
     * @param resource the {@code Resource} to hold (never {@code null})
     */
    public EncodedResource(Resource resource) {
        this(resource, null, null);
    }

    private EncodedResource(Resource resource, String encoding, Charset charset) {
        super();
        this.resource = resource;
        this.encoding = encoding;
        this.charset = charset;
    }

    public final Resource getResource() {
        return this.resource;
    }

    public final String getEncoding() {
        return this.encoding;
    }
}
