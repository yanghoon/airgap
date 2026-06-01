package io.slim.common;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class ResourceLoader {
    public static String load(String path) {
        try {
            if (path.startsWith("classpath:")) {
                try (InputStream is = ResourceLoader.class.getClassLoader().getResourceAsStream(path.substring(10))) {
                    if (is == null) throw new IOException("Resource not found: " + path);
                    return new String(is.readAllBytes(), StandardCharsets.UTF_8);
                }
            } else if (path.startsWith("file:")) {
                return Files.readString(Path.of(path.substring(5)), StandardCharsets.UTF_8);
            }
            throw new IllegalArgumentException("Prefix must be 'classpath:' or 'file:'");
        } catch (IOException e) {
            throw new UncheckedIOException("Failed to load resource: " + path, e);
        }
    }
}
