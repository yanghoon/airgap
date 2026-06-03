package io.slim.common;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Map;

import org.junit.jupiter.api.Test;

import io.slim.common.template.DefaultTemplateEngine;
import io.slim.common.template.TemplateContext;
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
        Map<String, Object> vars = Map.of("vars.key1", "value1", "vars.key2", "value2");

        TemplateEngine engine = new DefaultTemplateEngine();
        String template = "The value is ${format-map:path=vars, indent=2}";
        String result = engine.render(template, vars);

        assertEquals("""
        The value is ,
          'key2' = 'value2',
          'key1' = 'value1'
        """.strip(), result);
    }

    @Test
    public void testTemplateContextModifyVars() {
        var environment= JobContext.create().getEnvironment();
        var context = new TemplateContext(environment);
        context.computeIfPresent("env", String::toUpperCase);

        TemplateEngine engine = new DefaultTemplateEngine();
        String template = "Environment: ${env}";
        String result = engine.render(template, context.getVars());

        assertEquals("Environment: " + environment.env().toUpperCase(), result);
    }

    @Test
    public void testTemplateContextModifyVars2() {
        var environment= JobContext.create().getEnvironment();
        var context = new TemplateContext(environment);
        context.computeIfPresent("security.certificates", p -> ResourceLoader.load(p));

        TemplateEngine engine = new DefaultTemplateEngine();
        String template = "${security.certificates}";
        String result = engine.render(template, context.getVars());

        assertEquals("""
        -----BEGIN CERTIFICATE-----
        ABCD
        -----END CERTIFICATE-----
        """.strip(), result);
    }

}
