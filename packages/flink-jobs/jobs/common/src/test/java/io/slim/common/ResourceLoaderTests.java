package io.slim.common;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class ResourceLoaderTests {
    
    @Test
    public void testLoadClasspathResource() {
        String content = ResourceLoader.load("classpath:certs/ca.pem");
        assertEquals("""
        -----BEGIN CERTIFICATE-----
        ABCD
        -----END CERTIFICATE-----
        """.strip(), content);
    }

    @Test
    public void testLoadFileResource() {
        String content = ResourceLoader.load("file:src/test/resources/certs/ca.pem");
        assertEquals("""
        -----BEGIN CERTIFICATE-----
        ABCD
        -----END CERTIFICATE-----
        """.strip(), content);
    }

}
