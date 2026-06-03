package io.slim.common;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

public class JobContextTests {
    
    @Test
    public void testCreateJobContext() {
        var jobContext = JobContext.create();
        assertNotNull(jobContext);
        assertNotNull(jobContext.getEnvironment());
        assertNotNull(jobContext.getTemplateContext());
    }

    @Test
    public void testSqlManager() {
        var jobContext = JobContext.create();
        var sqlManager = jobContext.getSqlManager();
        assertNotNull(sqlManager);

        sqlManager.getRenderedSchemas().forEach(System.out::println);
        sqlManager.getRenderedPipelines().forEach(System.out::println);
    }

}
