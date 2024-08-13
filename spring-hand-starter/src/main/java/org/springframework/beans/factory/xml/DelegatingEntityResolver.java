package org.springframework.beans.factory.xml;

import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;

/**
 *
 */
public class DelegatingEntityResolver implements EntityResolver {

    private final EntityResolver dtdResolver;

    private final EntityResolver schemaResolver;



    public DelegatingEntityResolver(ClassLoader classLoader) {
        // 这里由于不做实现所以先给个null
        this.dtdResolver = null;
        this.schemaResolver = null;

        //TODO this.dtdResolver = new BeansDtdResolver();
        //TODO this.schemaResolver = new PluggableSchemaResolver(classLoader);
    }
    @Override
    public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
        return null;
    }
}
