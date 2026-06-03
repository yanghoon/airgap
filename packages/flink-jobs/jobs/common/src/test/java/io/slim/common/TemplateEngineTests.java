package io.slim.common;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Map;

import org.junit.jupiter.api.Test;

import io.slim.common.template.DefaultTemplateEngine;
import io.slim.common.template.TemplateEngine;

public class TemplateEngineTests {
    
    @Test
    public void testDefaultTemplateEngine() {
        TemplateEngine engine = new DefaultTemplateEngine();
        String template = "Hello, ${name}!";
        String result = engine.render(template, Map.of("name", "World"));
        assertEquals("Hello, World!", result);
    }

    @Test
    public void testFormatMapLookup() {
        String template = "The value is ${format-map:path=vars, indent=2}";
        Map<String, Object> vars = Map.of("vars.key1", "value1", "vars.key2", "value2");

        TemplateEngine engine = new DefaultTemplateEngine();
        String result = engine.render(template, vars);

        assertEquals("""
        The value is ,
          'key2' = 'value2',
          'key1' = 'value1'
        """.strip(), result);
    }

}
