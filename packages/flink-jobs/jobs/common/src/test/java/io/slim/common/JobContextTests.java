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

}
